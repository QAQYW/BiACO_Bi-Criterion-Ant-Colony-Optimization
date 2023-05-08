# BiACO_Bi-Criterion-Ant-Colony-Optimization

# 介绍
Bi-criterion Ant Colony Optimization algorithm.<br>
双目标的蚁群算法，可求得Pareto解集。<br>
用于一个TSP变种问题，有多种电量、时间窗等多种约束。<br>
做了简单的可视化。<br>
所有代码用Java写成，放置master下。<br>

# 程序入口
程序有几个不同入口，可自行选择，均位于main中<br>
ACO_DSPMain：用于和biACO对比的算法，没写main<br>
BiACOMain：单独运行biACO<br>
Main：算法对比的批量实验，计算4个Pareto解集的评价指标<br>
RunBatch：没写main<br>
VisualizationMain：单独运行biACO，并可视化
