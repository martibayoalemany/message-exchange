
### Benchmark of the different channels (from best to worse)

Benchmark 1_000 / 100_000                          Mode   Cnt    Score    Error    Units 
MultiProcessBenchmark.executeProcessBuffered       avgt    8   513.479 ±  462.307  ms/op 
MultiProcessBenchmark.executeProcessBufferedLarge  avgt    8  8553.014 ± 5603.001  ms/op 

Benchmark 1_000 / 100_000                             Mode  Cnt    Score     Error  Units 
SingleProcessBenchmarks.executeRecursive              avgt    8   ≈ 10⁻⁴            ms/op 
SingleProcessBenchmarks.executeRecursiveLarge         avgt    8    0.015 ±   0.001  ms/op 
SingleProcessBenchmarks.executeFairLarge              avgt    8    0.136 ±   0.003  ms/op
SingleProcessBenchmarks.executeUnfairLarge            avgt    8    0.145 ±   0.007  ms/op
SingleProcessBenchmarks.executeFair                   avgt    8    0.147 ±   0.023  ms/op
SingleProcessBenchmarks.executeUnfairLargeUnbuffered  avgt    8    0.176 ±   0.114  ms/op
SingleProcessBenchmarks.executeUnfair                 avgt    8    0.179 ±   0.098  ms/op
SingleProcessBenchmarks.executeUnfairUnbuffered       avgt    8    0.225 ±   0.026  ms/op
SingleProcessBenchmarks.executeAtomic                 avgt    8    0.407 ±   0.062  ms/op
SingleProcessBenchmarks.executeFairDeque              avgt    8    8.016 ±   1.261  ms/op
SingleProcessBenchmarks.executeAtomicLarge            avgt    8   21.901 ±   5.649  ms/op
SingleProcessBenchmarks.executeFairDequeLarge         avgt    8  934.789 ± 382.726  ms/op
