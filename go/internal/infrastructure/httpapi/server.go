package httpapi

import (
    "encoding/json"
    "errors"
    "net/http"
    "strings"

    "dev.ufadevconf/vo-go/internal/application"
    "dev.ufadevconf/vo-go/internal/domain"
)

// Server wires HTTP handlers to the application service.
type Server struct {
    app *application.PickupPointApplicationService
    mux *http.ServeMux
}

func NewServer(app *application.PickupPointApplicationService) *Server {
    s := &Server{app: app, mux: http.NewServeMux()}
    s.routes()
    return s
}

func (s *Server) ServeHTTP(w http.ResponseWriter, r *http.Request) {
    s.mux.ServeHTTP(w, r)
}

func (s *Server) routes() {
    s.mux.HandleFunc("/api/pickup-points", s.handlePickupPoints)
    s.mux.HandleFunc("/api/pickup-points/", s.handlePickupPointFitCheck)
}

func (s *Server) handlePickupPoints(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        methodNotAllowed(w)
        return
    }
    defer r.Body.Close()
    var req createPickupPointRequest
    if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
        writeError(w, http.StatusBadRequest, "invalid request body")
        return
    }
    minWeight, err := domain.WeightFromKilograms(req.MinWeightKg)
    if err != nil {
        writeError(w, http.StatusBadRequest, err.Error())
        return
    }
    maxWeight, err := domain.WeightFromKilograms(req.MaxWeightKg)
    if err != nil {
        writeError(w, http.StatusBadRequest, err.Error())
        return
    }
    command := application.CreatePickupPointCommand{
        Code:           req.Code,
        Address:        req.Address,
        MinWeightGrams: minWeight.Grams(),
        MaxWeightGrams: maxWeight.Grams(),
    }
    pickupPoint, err := s.app.RegisterPickupPoint(command)
    if err != nil {
        writeError(w, http.StatusBadRequest, err.Error())
        return
    }
    resp := pickupPointResponse{
        Code:           pickupPoint.Code(),
        Address:        pickupPoint.Address(),
        MinWeightGrams: pickupPoint.MinWeight().Grams(),
        MaxWeightGrams: pickupPoint.MaxWeight().Grams(),
    }
    writeJSON(w, http.StatusCreated, resp)
}

func (s *Server) handlePickupPointFitCheck(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        methodNotAllowed(w)
        return
    }
    code, ok := pickupPointCodeFromPath(r.URL.Path)
    if !ok {
        writeError(w, http.StatusNotFound, "pickup point code missing")
        return
    }
    defer r.Body.Close()
    var req parcelRequest
    if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
        writeError(w, http.StatusBadRequest, "invalid request body")
        return
    }
    query := application.CheckParcelFitsQuery{
        PickupPointCode:    code,
        TrackingNumber:     req.TrackingNumber,
        ContentsDescription: req.ContentsDescription,
        ParcelWeightGrams:  req.ParcelWeightGrams,
    }
    result, err := s.app.CheckParcelFits(query)
    if err != nil {
        if errors.Is(err, application.ErrPickupPointNotFound) {
            writeError(w, http.StatusNotFound, err.Error())
        } else {
            writeError(w, http.StatusBadRequest, err.Error())
        }
        return
    }
    resp := parcelFitResponse{
        PickupPointCode: result.PickupPointCode,
        TrackingNumber:  result.TrackingNumber,
        Fits:            result.Fits,
    }
    writeJSON(w, http.StatusOK, resp)
}

func pickupPointCodeFromPath(path string) (string, bool) {
    const prefix = "/api/pickup-points/"
    const suffix = "/fit-check"
    if !strings.HasPrefix(path, prefix) || !strings.HasSuffix(path, suffix) {
        return "", false
    }
    middle := strings.TrimSuffix(strings.TrimPrefix(path, prefix), suffix)
    if middle == "" || strings.Contains(middle, "/") {
        return "", false
    }
    return middle, true
}

func writeJSON(w http.ResponseWriter, status int, payload any) {
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(status)
    _ = json.NewEncoder(w).Encode(payload)
}

func writeError(w http.ResponseWriter, status int, message string) {
    writeJSON(w, status, map[string]string{"error": message})
}

func methodNotAllowed(w http.ResponseWriter) {
    writeError(w, http.StatusMethodNotAllowed, "method not allowed")
}

type createPickupPointRequest struct {
    Code         string  `json:"code"`
    Address      string  `json:"address"`
    MinWeightKg  float64 `json:"minWeightKg"`
    MaxWeightKg  float64 `json:"maxWeightKg"`
}

type parcelRequest struct {
    TrackingNumber     string `json:"trackingNumber"`
    ContentsDescription string `json:"contentsDescription"`
    ParcelWeightGrams  int64  `json:"parcelWeightGrams"`
}

type pickupPointResponse struct {
    Code           string `json:"code"`
    Address        string `json:"address"`
    MinWeightGrams int64  `json:"minWeightGrams"`
    MaxWeightGrams int64  `json:"maxWeightGrams"`
}

type parcelFitResponse struct {
    PickupPointCode string `json:"pickupPointCode"`
    TrackingNumber  string `json:"trackingNumber"`
    Fits            bool   `json:"fits"`
}
