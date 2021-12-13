# Bank Transaction System
## Bank Account Requirements
- You have been hired as a programmer by a major bank. Your first project is a small banking transaction system. Each account
consists of a number and a balance. The user of the program (the teller) can create a new account, as well as perform deposits,
withdrawals, and balance inquiries.
- Initially, the account information of existing customers is to be read into a pair of parallel arrays--one for account numbers, the
other for balances. The bank can handle up to MAX_NUM accounts.
- This method fills up the account number and balance arrays (up to maxAccts) by reading from an input file until EOF is reached,
and counting how many accounts are read in. It returns the actual number of accounts read in (later referred to as numAccts).
- After initialization, the main program prints the initial database of accounts and balances. Use method printAccts()
described below.
- The program then allows the user to select from the following menu of transactions:
  1. W - Withdrawal
  2. D - Deposit
  3. N - New account
  4. B - Balance
  5. Q – Quit
  6. X – Delete Account – Extra Credit
