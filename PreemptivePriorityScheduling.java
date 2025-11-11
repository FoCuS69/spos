import java.util.Scanner;

class Process {
    int pid;          // Process ID
    int arrivalTime;  // Arrival time
    int burstTime;    // CPU burst time
    int priority;     // Priority (lower value = higher priority)
    int remainingTime;// Remaining burst time
    int completionTime;
    int waitingTime;
    int turnaroundTime;
}

public class PreemptivePriorityScheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] processes = new Process[n];

        // Input process details
        for (int i = 0; i < n; i++) {
            processes[i] = new Process();
            processes[i].pid = i + 1;
            System.out.print("Enter arrival time for process " + (i + 1) + ": ");
            processes[i].arrivalTime = sc.nextInt();
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            processes[i].burstTime = sc.nextInt();
            processes[i].remainingTime = processes[i].burstTime;
            System.out.print("Enter priority for process " + (i + 1) + " (lower number means higher priority): ");
            processes[i].priority = sc.nextInt();
        }

        int completed = 0, currentTime = 0;
        int prevProcess = -1;
        int totalWaitingTime = 0, totalTurnaroundTime = 0;

        // Run until all processes are completed
        while (completed < n) {
            int highestPriority = Integer.MAX_VALUE;
            int idx = -1;

            // Find process with highest priority among arrived and unfinished processes
            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime && processes[i].remainingTime > 0) {
                    if (processes[i].priority < highestPriority) {
                        highestPriority = processes[i].priority;
                        idx = i;
                    }
                    // If same priority, choose process with earlier arrival time
                    else if (processes[i].priority == highestPriority) {
                        if (processes[i].arrivalTime < processes[idx].arrivalTime) {
                            idx = i;
                        }
                    }
                }
            }

            if (idx != -1) {
                // Run the selected process for 1 unit of time
                processes[idx].remainingTime--;
                currentTime++;

                // If process finished
                if (processes[idx].remainingTime == 0) {
                    completed++;
                    processes[idx].completionTime = currentTime;
                    processes[idx].turnaroundTime = processes[idx].completionTime - processes[idx].arrivalTime;
                    processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burstTime;

                    totalWaitingTime += processes[idx].waitingTime;
                    totalTurnaroundTime += processes[idx].turnaroundTime;
                }
            } else {
                // No process arrived yet, so just increment time
                currentTime++;
            }
        }

        // Print results
        System.out.println("\nPID\tArrival\tBurst\tPriority\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.printf("%d\t%d\t%d\t%d\t\t%d\t%d\n",
                    p.pid, p.arrivalTime, p.burstTime, p.priority, p.waitingTime, p.turnaroundTime);
        }

        System.out.printf("\nAverage Waiting Time: %.2f\n", (float) totalWaitingTime / n);
        System.out.printf("Average Turnaround Time: %.2f\n", (float) totalTurnaroundTime / n);

        sc.close();
    }
}

