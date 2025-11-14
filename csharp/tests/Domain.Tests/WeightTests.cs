using FluentAssertions;
using Vo.UfaDevConf.Domain;

namespace Vo.UfaDevConf.Domain.Tests;

public class WeightTests
{
    [Theory]
    [InlineData(-1)]
    [InlineData(-10)]
    public void NegativeWeightIsRejected(long grams)
    {
        var act = () => new Weight(grams);
        act.Should().Throw<ArgumentOutOfRangeException>();
    }

    [Fact]
    public void CreatesWeightFromKilogramsUsingHalfUpRounding()
    {
        var weight = Weight.FromKilograms(0.3335m);
        weight.Grams.Should().Be(334);
    }

    [Fact]
    public void ConvertsWeightToKilograms()
    {
        var weight = new Weight(2500);
        weight.ToKilograms().Should().Be(2.5m);
    }

    [Fact]
    public void ChecksBelongsToRangeRegardlessOfOrder()
    {
        var min = new Weight(3000);
        var max = new Weight(5000);
        var current = new Weight(4000);

        current.IsBetween(max, min).Should().BeTrue();
        current.IsBetween(min, max).Should().BeTrue();
        new Weight(1000).IsBetween(min, max).Should().BeFalse();
    }

    [Fact]
    public void WeightArithmeticProducesNewInstances()
    {
        var first = new Weight(1500);
        var second = new Weight(500);

        (first + second).Grams.Should().Be(2000);
        (first - second).Grams.Should().Be(1000);
    }

    [Fact]
    public void SubtractionCannotResultInNegativeWeight()
    {
        var light = new Weight(500);
        var heavy = new Weight(2000);

        var act = () => _ = light - heavy;
        act.Should().Throw<InvalidOperationException>();
    }
}
