namespace Vo.UfaDevConf.Application;

public sealed record CheckParcelFitsQuery(string PickupPointCode, string TrackingNumber, string ContentsDescription, long ParcelWeightGrams);
