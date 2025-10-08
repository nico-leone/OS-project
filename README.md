# Java Operating System Simulation

## Key Features
* **Process Management:** Processes (`UserlandProcess`) are multihreaded and use semaphores for cooperative multitasking.
* **Priority Scheduling:** Processes are manged in three queues (`RealTime`, `Interactive`, `Background`) and selected at random based off their weights.
* **Virtual Memory:** Implements a full **Paging** system with a **Translation Lookaside Buffer** and an **on-demand swapping mechanism** using a simulated disk.
* **Inter-Process Communication:** Supports synchronous **Message Passing** via kernel calls (`SendMessage`, `WaitForMessage`).
* **Deviec Management:** A **Virtual File System** abstracts device access to both a simulated disk (`FakeFileSystem`) and an I/O device (`RandomDevice`).

 ## Core Components
  
| Class | Role | Description |
| :--- | :--- | :--- |
| `OS.java` | **System Call Interface** | Provides API for userland processes to request kernel services (`CreateProcess`, `Sleep`, `Read`, `Write`, `SendMessage`). |
| `Kernel.java` | **OS Core** | The main OS loop that runs in its own thread, processing system calls from user processes and managing global resources. |
| `Scheduler.java` | **Process/CPU Manager** | Manages the three priority queues, handles the switching logic, manages the sleeping queue, and contains the core **Page Swap** logic. |
| `PCB.java` | **Process Control Block** | Stores all essential state information for a process: PID, Priority, Message Queue, and the **Virtual-to-Physical Page Table** (`memoryMap`). |
| `Hardware.java` | **Memory Hardware** | Simulates the physical hardware, containing the main `byte[] memory` array and low-level access functions. |
| `VFS.java` | **Device Abstraction Layer** | Routes I/O requests to the appropriate concrete device implementations. |

## Virtual Memory Implementation Details

1.  **Address Translation:** When a process reads or writes to a **Virtual Address**, the system first checks the 2-entry **TLB** in `UserlandProcess`.
2.  **Page Fault:** If the page is not in the TLB or the main memory, the process calls `OS.getMapping()`, triggering a page fault.
3.  **Page Swap:** The `Scheduler.pageSwap()` function finds a free physical page. If none is available, it selects a random victim process's page, swaps its content out to the `FakeFileSystem`'s swap file, frees the physical page, and then loads the requested page in.
4.  **Security:** `memoryTestB.java` verifies that processes are prevented from accessing memory outside of their allocated virtual address space.

## Running the Tests

The project includes several test drivers to demonstrate functionality:

* **`test.java`:** Demonstrates **priority scheduling** and the **sleeping** mechanism.
* **`PingPongTest.java`:** Demonstrates **Inter-Process Communication (IPC)** using `Ping` and `Pong` processes.
* **`PagingTest.java` / `VMtest.java`:** Demonstrates **Virtual Memory allocation, read/write, and swapping** under load.
