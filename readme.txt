Package simple
----------------------------------------------------------
- The most simple implementation it does locking via synchronized channel
- ThreadRunner is implemented
- ProcessRunner needs to be implemented

Package channelBased
----------------------------------------------------------
The code is a bit more complex, it tries different locking strategies and threading
Some parts are still unfinished


Benchmark of the different channels (from best to worse)
---------------------------------------------------------

It seems the best implementation is singleThread
The second best is *synchronized/unfair* where *fair* channel is a bit slower

Benchmark (10 pings)                                           Mode  Cnt   Score    Error  Units
channelBased.ApplicationBenchmark.singleThreaded                ss    8   2,450 ±  3,031  ms/op
channelBased.ApplicationBenchmark.unfairChannel                 ss    8   3,967 ±  3,342  ms/op
channelBased.ApplicationBenchmark.synchronizedChannel           ss    8   3,355 ±  4,020  ms/op
channelBased.ApplicationBenchmark.singleThreadedOneShot         ss    8   5,422 ±  5,026  ms/op
channelBased.ApplicationBenchmark.synchronizedChannelOneShot    ss    8  10,033 ± 13,376  ms/op
channelBased.ApplicationBenchmark.unfairChannelOneShot          ss    8  12,012 ± 26,495  ms/op
channelBased.ApplicationBenchmark.fairChannelOneShot            ss    8  14,949 ± 28,019  ms/op
channelBased.ApplicationBenchmark.fairChannel                   ss    8  19,366 ± 58,644  ms/op

Benchmark (10_000 pings)                                       Mode  Cnt    Score    Error  Units
channelBased.ApplicationBenchmark.singleThreaded                ss    8   16,499 ±  7,722  ms/op
channelBased.ApplicationBenchmark.synchronizedChannel           ss    8   20,122 ±  7,330  ms/op
channelBased.ApplicationBenchmark.unfairChannel                 ss    8   30,506 ± 24,301  ms/op
channelBased.ApplicationBenchmark.fairChannel                   ss    8   48,922 ± 61,928  ms/op
channelBased.ApplicationBenchmark.singleThreadedOneShot         ss    8  244,547 ± 16,817  ms/op
channelBased.ApplicationBenchmark.unfairChannelOneShot          ss    8  381,342 ± 38,794  ms/op
channelBased.ApplicationBenchmark.synchronizedChannelOneShot    ss    8  386,921 ± 71,205  ms/op
channelBased.ApplicationBenchmark.fairChannelOneShot            ss    8  404,323 ± 10,550  ms/op

Benchmark (10_000 pings)                           Mode  Cnt   Score    Error  Units
SimpleApplicationBenchmark.execute                   ss    8  36,236 ± 48,524  ms/op

Benchmark  (100_000 pings)                           Mode  Cnt    Score     Error  Units
SimpleApplicationBenchmark.execute                    ss    8  424,617 ± 201,946  ms/op