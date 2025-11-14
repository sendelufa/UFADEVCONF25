namespace Vo.UfaDevConf.Application;

public sealed record CreatePickupPointCommand(string Code, string Address, long MinWeightGrams, long MaxWeightGrams);
