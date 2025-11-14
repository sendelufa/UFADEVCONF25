using Vo.UfaDevConf.Domain;

namespace Vo.UfaDevConf.Application;

public sealed class PickupPointApplicationService
{
    private readonly IPickupPointRepository _repository;

    public PickupPointApplicationService(IPickupPointRepository repository)
    {
        _repository = repository ?? throw new ArgumentNullException(nameof(repository));
    }

    public async Task<PickupPoint> RegisterPickupPointAsync(CreatePickupPointCommand command, CancellationToken cancellationToken = default)
    {
        ArgumentNullException.ThrowIfNull(command);
        if (command.MinWeightGrams < 0)
        {
            throw new ArgumentOutOfRangeException(nameof(command.MinWeightGrams));
        }
        if (command.MaxWeightGrams < 0)
        {
            throw new ArgumentOutOfRangeException(nameof(command.MaxWeightGrams));
        }

        var pickupPoint = new PickupPoint(
            command.Code ?? throw new ArgumentNullException(nameof(command.Code)),
            command.Address ?? throw new ArgumentNullException(nameof(command.Address)),
            new Weight(command.MinWeightGrams),
            new Weight(command.MaxWeightGrams));

        await _repository.SaveAsync(pickupPoint, cancellationToken);
        return pickupPoint;
    }

    public async Task<ParcelFitResult> CheckParcelFitsAsync(CheckParcelFitsQuery query, CancellationToken cancellationToken = default)
    {
        ArgumentNullException.ThrowIfNull(query);
        var pickupPoint = await _repository.FindByCodeAsync(query.PickupPointCode ?? throw new ArgumentNullException(nameof(query.PickupPointCode)), cancellationToken);
        if (pickupPoint is null)
        {
            throw new PickupPointNotFoundException(query.PickupPointCode);
        }

        var parcel = new Parcel(
            query.TrackingNumber ?? throw new ArgumentNullException(nameof(query.TrackingNumber)),
            new Weight(query.ParcelWeightGrams),
            query.ContentsDescription ?? throw new ArgumentNullException(nameof(query.ContentsDescription)));

        var fits = pickupPoint.CanAccept(parcel);
        return new ParcelFitResult(pickupPoint.Code, parcel.TrackingNumber, fits);
    }
}
