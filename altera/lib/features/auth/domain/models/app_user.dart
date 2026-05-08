class AppUser {
  final String id;
  final String email;
  final String? fullName;
  final String? avatarUrl;

  AppUser({
    required this.id,
    required this.email,
    this.fullName,
    this.avatarUrl,
  });

  factory AppUser.fromSupabase(Map<String, dynamic> json) {
    return AppUser(
      id: json['id'],
      email: json['email'],
      fullName: json['full_name'],
      avatarUrl: json['avatar_url'],
    );
  }
}
