using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Vo.UfaDevConf.Application;
using Vo.UfaDevConf.Domain;
using Vo.UfaDevConf.Infrastructure.Repositories;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton<IPickupPointRepository, InMemoryPickupPointRepository>();
builder.Services.AddScoped<PickupPointApplicationService>();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

var pickupPoints = app.MapGroup("/api/pickup-points");

pickupPoints.MapPost("", async Task<Results<Created<PickupPointResponse>, BadRequest<ProblemDetails>>> (
    CreatePickupPointRequest request,
    PickupPointApplicationService service,
    CancellationToken cancellationToken) =>
{
    try
    {
        var minWeight = Weight.FromKilograms(request.MinWeightKg);
        var maxWeight = Weight.FromKilograms(request.MaxWeightKg);
        var command = new CreatePickupPointCommand(request.Code, request.Address, minWeight.Grams, maxWeight.Grams);
        var pickupPoint = await service.RegisterPickupPointAsync(command, cancellationToken);
        var response = new PickupPointResponse(pickupPoint.Code, pickupPoint.Address, pickupPoint.MinWeight.Grams, pickupPoint.MaxWeight.Grams);
        return TypedResults.Created($"/api/pickup-points/{response.Code}", response);
    }
    catch (Exception ex) when (ex is ArgumentException or ArgumentNullException or ArgumentOutOfRangeException or InvalidOperationException)
    {
        return TypedResults.BadRequest(new ProblemDetails { Title = "Invalid payload", Detail = ex.Message });
    }
});

pickupPoints.MapPost("/{code}/fit-check", async Task<Results<Ok<ParcelFitResponse>, NotFound<ProblemDetails>, BadRequest<ProblemDetails>>> (
    string code,
    ParcelRequest request,
    PickupPointApplicationService service,
    CancellationToken cancellationToken) =>
{
    try
    {
        var query = new CheckParcelFitsQuery(code, request.TrackingNumber, request.ContentsDescription, request.ParcelWeightGrams);
        var result = await service.CheckParcelFitsAsync(query, cancellationToken);
        var response = new ParcelFitResponse(result.PickupPointCode, result.TrackingNumber, result.Fits);
        return TypedResults.Ok(response);
    }
    catch (PickupPointNotFoundException ex)
    {
        return TypedResults.NotFound(new ProblemDetails { Title = "Pickup point not found", Detail = ex.Message });
    }
    catch (Exception ex) when (ex is ArgumentException or ArgumentNullException or ArgumentOutOfRangeException or InvalidOperationException)
    {
        return TypedResults.BadRequest(new ProblemDetails { Title = "Invalid payload", Detail = ex.Message });
    }
});

app.Run();

internal sealed record CreatePickupPointRequest(string Code, string Address, decimal MinWeightKg, decimal MaxWeightKg);

internal sealed record ParcelRequest(string TrackingNumber, string ContentsDescription, long ParcelWeightGrams);

internal sealed record PickupPointResponse(string Code, string Address, long MinWeightGrams, long MaxWeightGrams);

internal sealed record ParcelFitResponse(string PickupPointCode, string TrackingNumber, bool Fits);
