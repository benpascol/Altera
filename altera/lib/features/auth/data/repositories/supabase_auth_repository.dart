import 'dart:async';
import 'package:supabase_flutter/supabase_flutter.dart';
import '../../domain/models/app_user.dart';
import '../../domain/repositories/auth_repository.dart';

class SupabaseAuthRepository implements AuthRepository {
  final SupabaseClient _client;

  SupabaseAuthRepository(this._client);

  @override
  AppUser? get currentUser {
    final user = _client.auth.currentUser;
    if (user == null) return null;
    return AppUser(
      id: user.id,
      email: user.email ?? '',
      fullName: user.userMetadata?['full_name'],
      avatarUrl: user.userMetadata?['avatar_url'],
    );
  }

  @override
  Stream<AppUser?> get authStateChanges => _client.auth.onAuthStateChange.map((data) {
        final user = data.session?.user;
        if (user == null) return null;
        return AppUser(
          id: user.id,
          email: user.email ?? '',
          fullName: user.userMetadata?['full_name'],
          avatarUrl: user.userMetadata?['avatar_url'],
        );
      });

  @override
  Future<AppUser?> signInWithEmail({required String email, required String password}) async {
    final response = await _client.auth.signInWithPassword(email: email, password: password);
    final user = response.user;
    if (user == null) return null;
    return AppUser(
      id: user.id,
      email: user.email ?? '',
      fullName: user.userMetadata?['full_name'],
    );
  }

  @override
  Future<AppUser?> signUpWithEmail({required String email, required String password, String? fullName}) async {
    final response = await _client.auth.signUp(
      email: email, 
      password: password,
      data: {'full_name': fullName},
    );
    final user = response.user;
    if (user == null) return null;
    return AppUser(
      id: user.id,
      email: user.email ?? '',
      fullName: fullName,
    );
  }

  @override
  Future<void> signOut() async {
    await _client.auth.signOut();
  }
}
