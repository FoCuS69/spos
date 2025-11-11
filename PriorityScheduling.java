import java.util.*;

class Process {
    int pid, burstTime, priority, waitingTime, turnAroundTime;

    Process(int pid, int burstTime, int priority)
     {
        this.pid = pid;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class PriorityScheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] processes = new Process[n];
        
        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1));
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            System.out.print("Priority (lower number = higher priority): ");
            int pr = sc.nextInt();
            processes[i] = new Process(i + 1, bt, pr);
        }

        // Sort processes by priority
        Arrays.sort(processes, Comparator.comparingInt(p -> p.priority));

        // Calculate waiting time and turnaround time
        processes[0].waitingTime = 0;
        processes[0].turnAroundTime = processes[0].burstTime;
        
        for (int i = 1; i < n; i++) {
            processes[i].waitingTime = processes[i-1].waitingTime + processes[i-1].burstTime;
            processes[i].turnAroundTime = processes[i].waitingTime + processes[i].burstTime;
        }

        // Display results
        System.out.println("\nPID\tBT\tPriority\tWT\tTAT");
        int totalWT = 0, totalTAT = 0;
        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnAroundTime;
            System.out.println(p.pid + "\t" + p.burstTime + "\t" + p.priority + "\t\t" + p.waitingTime + "\t" + p.turnAroundTime);
        }

        System.out.printf("\nAverage Waiting Time = %.2f\n", (float)totalWT / n);
        System.out.printf("Average Turnaround Time = %.2f\n", (float)totalTAT / n);

        sc.close();
    }
}

