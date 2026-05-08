import '../models/app_user.dart';

abstract class AuthRepository {
  Future<AppUser?> signInWithEmail({required String email, required String password});
  Future<AppUser?> signUpWithEmail({required String email, required String password, String? fullName});
  Future<void> signOut();
  Stream<AppUser?> get authStateChanges;
  AppUser? get currentUser;
}
