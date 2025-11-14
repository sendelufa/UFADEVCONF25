namespace Vo.UfaDevConf.Application;

public sealed class PickupPointNotFoundException : Exception
{
    public PickupPointNotFoundException(string code)
        : base($"Pickup point not found: {code}")
    {
    }
}
