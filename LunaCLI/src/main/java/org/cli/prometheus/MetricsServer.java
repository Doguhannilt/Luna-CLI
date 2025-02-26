package org.cli.prometheus;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import static org.cli.manager.CommandHistory.getHistorySize;

public class MetricsServer {

    private static final Counter requestCounter = Counter.build()
            .name("total_requests")
            .help("Number of requests")
            .register();

    private static final Gauge historySizeGauge = Gauge.build()
            .name("cli_command_history_size")
            .help("CLI Command counter")
            .register();

    private static final Gauge memoryUsageGauge = Gauge.build()
            .name("system_memory_usage_mb")
            .help("System memory usage in MB")
            .register();

    private static final Gauge cpuUsageGauge = Gauge.build()
            .name("system_cpu_usage")
            .help("CPU usage percentage")
            .register();

    private static final Gauge loadAverageGauge = Gauge.build()
            .name("system_load_average")
            .help("System load average")
            .register();

    private static final OperatingSystemMXBean osBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public static void main(String[] args) throws IOException {
        HTTPServer server = new HTTPServer(9091);



        new Thread(() -> {
            while (true) {
                updateMetrics();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private static void updateMetrics() {
        int historySize = getHistorySize();
        requestCounter.inc();
        historySizeGauge.set(historySize);


        double totalMemory = osBean.getTotalPhysicalMemorySize() / (1024.0 * 1024.0);
        double freeMemory = osBean.getFreePhysicalMemorySize() / (1024.0 * 1024.0);
        double usedMemory = totalMemory - freeMemory;
        memoryUsageGauge.set(usedMemory);

        double cpuLoad = osBean.getSystemCpuLoad() * 100;
        if (cpuLoad >= 0) {
            cpuUsageGauge.set(cpuLoad);
        }


        double systemLoad = osBean.getSystemLoadAverage();
        if (systemLoad >= 0) {
            loadAverageGauge.set(systemLoad);
        }
    }
}
