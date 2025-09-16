# Code for "Geometric Freeze-Tag Problem"

This repository contains the official source code and experimental data for the paper:

> **"Geometric Freeze-Tag Problem"** > *Submitted to the Journal of Autonomous Agents and Multiagent Systems (JAAMAS)*

## Overview

The **Freeze-Tag Problem (FTP)** is a classic scheduling problem where active "robots" must activate frozen ones to minimize the total time (makespan) required to activate everyone.

This repository provides two main sets of code:
1.  The Java implementation of our proposed **(R², ℓ₂) strategies**, used to generate the real-world experimental results in Section 6 of our paper.
2.  Supporting C/C++ code for theoretical calculations related to the Euclidean variant of the problem (E-FTP).

---

## Repository Structure

* **`Calculations/`**: Contains core solvers and computational notebooks for theoretical analysis.
    * `EFTPSolver.c`: Computing the theoretical makespan bound of the **Euclidean Freeze-Tag Problem (E-FTP)**. The results from this solver are detailed in the "Additional Calculations" section below.
    * `GeodesicToEucledeanRatioCalculator.ipynb`: Used for our calculations on the ratio between geodesic and Euclidean distances.
    * `MakespanCalculator.cpp`: A utility to calculate the makespan for FTP under various norms in different dimensions via optimized exhastive search.

* **`Experimental Results/`**: Contains the Java implementation of our paper's main contribution.
    * `R2L2TreeMaker.java`, `Input.java`, `Utility.java`: These files implement the **(R², ℓ₂) strategies** evaluated in our paper. They were used to solve FTP instances using real-world spatial data.

---

## Experimental Results

We evaluated the performance of our proposed **(R², ℓ₂) strategies** on real-world spatial data to assess their effectiveness in practical, geographically distributed scenarios. The following experiments were conducted using the Java code in the `Experimental Results/` directory.

### Experiment 1: NYC Pharmacy Medication Distribution 

This experiment simulated a public health emergency where a vital medication, initially at one pharmacy, needed to be distributed to all other pharmacies in New York City.

* **Scenario**: Modeled the activation process of FTP as pharmacies receiving medication from an already-supplied location.
* **Data**: Pharmacy locations sourced from Google Maps, with Times Square set as the origin (0, 0).
* **Result**: The algorithm successfully distributed the medication to all locations with a makespan of approximately **1.225 time units**.

### Experiment 2: University Campus Information Spread 

This experiment modeled how an urgent announcement spreads from a single point to key locations across a university campus.

* **Scenario**: Modeled information spread, where a location becomes "informed" by contact from an already-informed location.
* **Data**: High-traffic campus locations (classrooms, libraries) with spatial data from Google Earth.
* **Result**: The algorithm propagated the information to all target locations in approximately **2.76 time units**.

These results confirm that our proposed strategies provide efficient solutions for real-world instances of the Freeze-Tag Problem.

---

## Additional Calculations: E-FTP Makespan Bound

This repository also contains `Calculations/EFTPSolver.c`, a program to compute the maximum makespan for the theoretical **Euclidean Freeze-Tag Problem (E-FTP)**. The results below correspond to this specific problem and were part of our preliminary findings.

#### Case 1: $r_3 < 0.66$

| Interval    | Last Log                                             |
| :---------- | :--------------------------------------------------- |
| 0.55–0.66   | (0.560, 0.560, 0.560, 2.090, 2.090, **4.2773**)       |

#### Case 2: $r_3 \ge 0.66$

| Interval    | Last Log                                             |
| :---------- | :--------------------------------------------------- |
| 0.00–0.11   | (0.000, 0.830, 0.920, 0.000, 0.000, **4.2699**)       |

* The maximum theoretical makespan found for the E-FTP was **4.2773**.

---

## System Specifications

The experiments were conducted on a machine with the following specifications:

-   **CPU**: 8 cores of an Intel Xeon Gold 6230
-   **RAM**: 32 GB
-   **Total Runtime**: Approximately 40 hours

