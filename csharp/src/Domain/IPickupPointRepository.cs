namespace Vo.UfaDevConf.Domain;

public interface IPickupPointRepository
{
    Task SaveAsync(PickupPoint pickupPoint, CancellationToken cancellationToken = default);

    Task<PickupPoint?> FindByCodeAsync(string code, CancellationToken cancellationToken = default);
}
