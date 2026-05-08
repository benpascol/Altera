import '../models/task.dart';

abstract class TaskRepository {
  Future<List<Task>> getTasks();
  Future<Task> createTask(Task task);
  Future<void> updateTask(Task task);
  Future<void> deleteTask(String taskId);
  Stream<List<Task>> watchTasks();
}
