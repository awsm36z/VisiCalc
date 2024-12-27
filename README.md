# VisiCalc

VisiCalc is a simple spreadsheet application implemented in Java, inspired by the original VisiCalc program. It demonstrates fundamental concepts in software engineering such as data structures, algorithms, and user interface design.

By Yassine El Yacoubi
https://bit.ly/yassine-blogs
https://musaim.ai

## Features

- Supports basic cell types: text, number, date, and formula.
- Allows setting and retrieving cell values.
- Basic formula evaluation (addition, subtraction, multiplication, division).
- Command-line interface for interacting with the spreadsheet.
- Demonstrates the use of data structures like arrays and hash maps.
- Implements algorithms for formula parsing and evaluation.
- Showcases basic principles of user interface design in a command-line environment.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/awsm36z/Visi
   ```

### Usage

To run the application, navigate to the project directory and execute the following command:
```bash
java -jar VisiCalc.jar
```

### Commands

- **Set a cell value**: To set a value in a specific cell, use the following command:
  ```bash
  set <cell> <value>
  ```
  Example:
  ```bash
  set A1 10
  ```

- **Get a cell value**: To retrieve the value of a specific cell, use the following command:
  ```bash
  get <cell>
  ```
  Example:
  ```bash
  get A1
  ```

- **Evaluate a formula**: To evaluate a formula in a specific cell, use the following command:
  ```bash
  set <cell> <formula>
  ```
  Example:
  ```bash
  set B1 =A1+5
  ```

- **Display the spreadsheet**: To display the current state of the spreadsheet, use the following command:
  ```bash
  display
  ```

### Example Operations

1. Set the value of cell A1 to 10:
   ```bash
   set A1 10
   ```

2. Set the value of cell B1 to 20:
   ```bash
   set B1 20
   ```

3. Set the formula in cell C1 to the sum of A1 and B1:
   ```bash
   set C1 =A1+B1
   ```

4. Retrieve the value of cell C1:
   ```bash
   get C1
   ```

5. Display the entire spreadsheet:
   ```bash
   display
   ```

