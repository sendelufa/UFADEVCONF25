namespace Vo.UfaDevConf.Domain;

public sealed class PickupPoint
{
    public PickupPoint(string code, string address, Weight minWeight, Weight maxWeight)
    {
        Code = code ?? throw new ArgumentNullException(nameof(code));
        Address = address ?? throw new ArgumentNullException(nameof(address));
        MinWeight = minWeight;
        MaxWeight = maxWeight;

        if (MinWeight.CompareTo(MaxWeight) > 0)
        {
            throw new ArgumentException("Min weight cannot exceed max weight");
        }
    }

    public string Code { get; }

    public string Address { get; }

    public Weight MinWeight { get; }

    public Weight MaxWeight { get; }

    public bool CanAccept(Parcel parcel)
    {
        ArgumentNullException.ThrowIfNull(parcel);
        return parcel.Weight.IsBetween(MinWeight, MaxWeight);
    }
}
