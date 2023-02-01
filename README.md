# Points-intern

A program that reads from a CSV file called transactions.csv in the current working directory, processes an argument, and returns a response based on the conditions:
  - Spend points based on the argument using the rules.
    - the oldest points to be spent first (oldest based on transaction timestamp, not the order they’re received).
    - no payer's points to go negative.
  - Return all payer point balances.


## CSV file - transactions.csv
Note that the first line of the file will be the names of the columns.

Example:

  ```
  payer,points,timestamp
  DANNON,1000,2020-11-02T14:00:00Z
  UNILEVER,200,2020-10-31T11:00:00Z
  DANNON,-200,2020-10-31T15:00:00Z
  MILLER COORS,10000,2020-11-01T14:00:00Z
  DANNON,300,2020-10-31T10:00:00Z
  ```
## Execute program
In terminal, navigate to the file containing the java file and csv file.

Then, run the commands:

`javac GetPoints.java`

`java GetPoints POINTS` (with the amount of points to spend as positive integer, e.g. `java GetPoints 5000`)

## Example output
```
{ 
  "DANNON": "1000", 
  "UNILEVER": "0", 
  "MILLER COORS": "5300" 
} 
```
