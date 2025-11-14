using FluentAssertions;
using Vo.UfaDevConf.Domain;

namespace Vo.UfaDevConf.Domain.Tests;

public class PickupPointTests
{
    [Fact]
    public void AcceptsParcelWithinRange()
    {
        var pickupPoint = new PickupPoint(
            "SPB-101",
            "Санкт-Петербург, Невский проспект, 1",
            new Weight(1000),
            new Weight(5000));

        var fits = pickupPoint.CanAccept(new Parcel("TRACK-1", new Weight(2000), "Документы"));
        var rejects = pickupPoint.CanAccept(new Parcel("TRACK-2", new Weight(7000), "Инструменты"));

        fits.Should().BeTrue();
        rejects.Should().BeFalse();
    }

    [Fact]
    public void RejectsParcelBelowMinimum()
    {
        var pickupPoint = new PickupPoint(
            "SPB-102",
            "Санкт-Петербург, Литейный пр., 5",
            new Weight(2000),
            new Weight(6000));

        var tooLight = new Parcel("TRACK-3", new Weight(1500), "Образцы");
        pickupPoint.CanAccept(tooLight).Should().BeFalse();
    }

    [Fact]
    public void ThrowsWhenMinGreaterThanMax()
    {
        var act = () => new PickupPoint("SPB", "Адрес", new Weight(5000), new Weight(1000));
        act.Should().Throw<ArgumentException>();
    }
}
