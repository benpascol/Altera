import 'package:supabase_flutter/supabase_flutter.dart';
import '../../domain/models/task.dart';
import '../../domain/repositories/task_repository.dart';

class SupabaseTaskRepository implements TaskRepository {
  final SupabaseClient _client;

  SupabaseTaskRepository(this._client);

  @override
  Future<List<Task>> getTasks() async {
    final response = await _client
        .from('tasks')
        .select()
        .order('created_at', ascending: false);
    
    return (response as List).map((json) => Task.fromSupabase(json)).toList();
  }

  @override
  Future<Task> createTask(Task task) async {
    final response = await _client
        .from('tasks')
        .insert(task.toSupabase())
        .select()
        .single();
    
    return Task.fromSupabase(response);
  }

  @override
  Future<void> updateTask(Task task) async {
    await _client
        .from('tasks')
        .update(task.toSupabase())
        .eq('id', task.id);
  }

  @override
  Future<void> deleteTask(String taskId) async {
    await _client.from('tasks').delete().eq('id', taskId);
  }

  @override
  Stream<List<Task>> watchTasks() {
    return _client
        .from('tasks')
        .stream(primaryKey: ['id'])
        .order('created_at', ascending: false)
        .map((data) => data.map((json) => Task.fromSupabase(json)).toList());
  }
}
