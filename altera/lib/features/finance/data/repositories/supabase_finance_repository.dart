import 'package:supabase_flutter/supabase_flutter.dart';
import '../../domain/models/transaction.dart';

class SupabaseTransactionRepository {
  final SupabaseClient _client;

  SupabaseTransactionRepository(this._client);

  Future<List<Transaction>> getTransactions() async {
    final response = await _client
        .from('transactions')
        .select()
        .order('transaction_date', ascending: false);
    
    return (response as List).map((json) => Transaction.fromSupabase(json)).toList();
  }

  Future<void> addTransaction(Transaction transaction) async {
    await _client.from('transactions').insert(transaction.toSupabase());
  }

  Stream<List<Transaction>> watchTransactions() {
    return _client
        .from('transactions')
        .stream(primaryKey: ['id'])
        .order('transaction_date', ascending: false)
        .map((data) => data.map((json) => Transaction.fromSupabase(json)).toList());
  }
}
