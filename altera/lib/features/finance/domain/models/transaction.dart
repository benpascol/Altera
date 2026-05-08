class Transaction {
  final String id;
  final String userId;
  final double amount;
  final String category;
  final String? description;
  final String type; // 'income' or 'expense'
  final DateTime transactionDate;

  Transaction({
    required this.id,
    required this.userId,
    required this.amount,
    required this.category,
    this.description,
    required this.type,
    required this.transactionDate,
  });

  factory Transaction.fromSupabase(Map<String, dynamic> json) {
    return Transaction(
      id: json['id'].toString(),
      userId: json['user_id'],
      amount: (json['amount'] as num).toDouble(),
      category: json['category'],
      description: json['description'],
      type: json['type'],
      transactionDate: DateTime.parse(json['transaction_date']),
    );
  }

  Map<String, dynamic> toSupabase() {
    return {
      'amount': amount,
      'category': category,
      'description': description,
      'type': type,
      'transaction_date': transactionDate.toIso8601String(),
    };
  }
}
