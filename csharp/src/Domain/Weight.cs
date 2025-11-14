namespace Vo.UfaDevConf.Domain;

/// <summary>
/// Value Object representing parcel weight in grams with helper conversions and arithmetic.
/// </summary>
public readonly record struct Weight : IComparable<Weight>
{
    public long Grams { get; init; }

    public Weight(long grams)
    {
        if (grams < 0)
        {
            throw new ArgumentOutOfRangeException(nameof(grams), grams, "Weight cannot be negative");
        }

        Grams = grams;
    }

    public static Weight FromKilograms(decimal kilograms)
    {
        var grams = decimal.Round(kilograms * 1000m, 0, MidpointRounding.AwayFromZero);
        return new Weight((long)grams);
    }

    public decimal ToKilograms() => Grams / 1000m;

    public bool IsBetween(Weight first, Weight second)
    {
        var lower = Math.Min(first.Grams, second.Grams);
        var upper = Math.Max(first.Grams, second.Grams);
        return Grams >= lower && Grams <= upper;
    }

    public static Weight operator +(Weight left, Weight right) => new(left.Grams + right.Grams);

    public static Weight operator -(Weight left, Weight right)
    {
        var result = left.Grams - right.Grams;
        return new Weight(result);
    }

    public int CompareTo(Weight other) => Grams.CompareTo(other.Grams);
}
