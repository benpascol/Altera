enum TaskPriority { low, medium, high }

class Task {
  final String id;
  final String userId;
  final String title;
  final String? description;
  final DateTime? dueDate;
  final bool isCompleted;
  final TaskPriority priority;
  final DateTime createdAt;

  Task({
    required this.id,
    required this.userId,
    required this.title,
    this.description,
    this.dueDate,
    this.isCompleted = false,
    this.priority = TaskPriority.medium,
    required this.createdAt,
  });

  factory Task.fromSupabase(Map<String, dynamic> json) {
    return Task(
      id: json['id'].toString(),
      userId: json['user_id'],
      title: json['title'],
      description: json['description'],
      dueDate: json['due_date'] != null ? DateTime.parse(json['due_date']) : null,
      isCompleted: json['is_completed'] ?? false,
      priority: TaskPriority.values.firstWhere(
        (e) => e.name == (json['priority'] ?? 'medium'),
        orElse: () => TaskPriority.medium,
      ),
      createdAt: DateTime.parse(json['created_at']),
    );
  }

  Map<String, dynamic> toSupabase() {
    return {
      'user_id': userId,
      'title': title,
      'description': description,
      'due_date': dueDate?.toIso8601String(),
      'is_completed': isCompleted,
      'priority': priority.name,
    };
  }
}
