import java.util.Arrays;
import java.util.Scanner;

public class MemoryAllocation {

    // First Fit
    static void firstFit(int[] blockSize, int m, int[] processSize, int n) {
        int[] allocation = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (blockSize[j] >= processSize[i]) {
                    allocation[i] = j;            // allocate block j to process i
                    blockSize[j] -= processSize[i];
                    break;
                }
            }
        }

        System.out.println("\n--- First Fit Allocation ---");
        printAllocation(processSize, allocation);
    }

    // Best Fit
    static void bestFit(int[] blockSize, int m, int[] processSize, int n) {
        int[] allocation = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            int bestIdx = -1;
            for (int j = 0; j < m; j++) {
                if (blockSize[j] >= processSize[i]) {
                    if (bestIdx == -1 || blockSize[j] < blockSize[bestIdx]) {
                        bestIdx = j;
                    }
                }
            }
            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                blockSize[bestIdx] -= processSize[i];
            }
        }

        System.out.println("\n--- Best Fit Allocation ---");
        printAllocation(processSize, allocation);
    }

    // Worst Fit
    static void worstFit(int[] blockSize, int m, int[] processSize, int n) {
        int[] allocation = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            int worstIdx = -1;
            for (int j = 0; j < m; j++) {
                if (blockSize[j] >= processSize[i]) {
                    if (worstIdx == -1 || blockSize[j] > blockSize[worstIdx]) {
                        worstIdx = j;
                    }
                }
            }
            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                blockSize[worstIdx] -= processSize[i];
            }
        }

        System.out.println("\n--- Worst Fit Allocation ---");
        printAllocation(processSize, allocation);
    }

    // Next Fit
    static void nextFit(int[] blockSize, int m, int[] processSize, int n) {
        int[] allocation = new int[n];
        Arrays.fill(allocation, -1);

        int start = 0; // pointer where next search begins (last allocation index)
        for (int i = 0; i < n; i++) {
            boolean allocated = false;
            for (int k = 0; k < m; k++) {
                int j = (start + k) % m;
                if (blockSize[j] >= processSize[i]) {
                    allocation[i] = j;
                    blockSize[j] -= processSize[i];
                    start = j; // next search will start from this block
                    allocated = true;
                    break;
                }
            }
            if (!allocated) {
                allocation[i] = -1;
            }
        }

        System.out.println("\n--- Next Fit Allocation ---");
        printAllocation(processSize, allocation);
    }

    // Utility to print results
    static void printAllocation(int processSize[], int allocation[]) {
        System.out.println("Process No.\tProcess Size\tBlock No.");
        for (int i = 0; i < processSize.length; i++) {
            System.out.print((i + 1) + "\t\t" + processSize[i] + "\t\t");
            if (allocation[i] != -1) {
                System.out.println(allocation[i] + 1); // display block number as 1-based
            } else {
                System.out.println("Not Allocated");
            }
        }
    }

    // Main: input + call each strategy
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of blocks: ");
        int m = sc.nextInt();
        int[] blockSize = new int[m];
        System.out.println("Enter block sizes:");
        for (int i = 0; i < m; i++) blockSize[i] = sc.nextInt();

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] processSize = new int[n];
        System.out.println("Enter process sizes:");
        for (int i = 0; i < n; i++) processSize[i] = sc.nextInt();

        // Use clones so each algorithm gets the original block sizes
        firstFit(blockSize.clone(), m, processSize, n);
        bestFit(blockSize.clone(), m, processSize, n);
        worstFit(blockSize.clone(), m, processSize, n);
        nextFit(blockSize.clone(), m, processSize, n);

        sc.close();
    }
}
