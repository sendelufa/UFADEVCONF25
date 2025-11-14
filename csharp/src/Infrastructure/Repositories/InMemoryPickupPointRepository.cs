using System.Collections.Concurrent;
using Vo.UfaDevConf.Domain;

namespace Vo.UfaDevConf.Infrastructure.Repositories;

public sealed class InMemoryPickupPointRepository : IPickupPointRepository
{
    private readonly ConcurrentDictionary<string, PickupPoint> _storage = new(StringComparer.OrdinalIgnoreCase);

    public Task SaveAsync(PickupPoint pickupPoint, CancellationToken cancellationToken = default)
    {
        _storage[pickupPoint.Code] = pickupPoint;
        return Task.CompletedTask;
    }

    public Task<PickupPoint?> FindByCodeAsync(string code, CancellationToken cancellationToken = default)
    {
        _storage.TryGetValue(code, out var pickupPoint);
        return Task.FromResult(pickupPoint);
    }
}
