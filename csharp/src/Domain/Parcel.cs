namespace Vo.UfaDevConf.Domain;

public sealed record Parcel
{
    public Parcel(string trackingNumber, Weight weight, string contentsDescription)
    {
        TrackingNumber = trackingNumber ?? throw new ArgumentNullException(nameof(trackingNumber));
        Weight = weight;
        ContentsDescription = contentsDescription ?? throw new ArgumentNullException(nameof(contentsDescription));
    }

    public string TrackingNumber { get; }

    public Weight Weight { get; }

    public string ContentsDescription { get; }
}
