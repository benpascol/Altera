import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import '../../../../presentation/widgets/glass_card.dart';
import '../providers/auth_providers.dart';

class LoginScreen extends ConsumerStatefulWidget {
  const LoginScreen({super.key});

  @override
  ConsumerState<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends ConsumerState<LoginScreen> {
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();
  final _fullNameController = TextEditingController();
  bool _isRegistering = false;

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    _fullNameController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authControllerProvider);

    // Listen for state changes to show notifications
    ref.listen<AsyncValue<void>>(authControllerProvider, (previous, next) {
      if (next is AsyncError) {
        String errorMessage = next.error.toString();
        
        // Handle specific Supabase Auth Errors for better UX
        if (errorMessage.contains('over_email_send_rate_limit') || 
            errorMessage.contains('rate_limit') || 
            errorMessage.contains('429')) {
          errorMessage = "Slow down! Too many requests. Please wait about a minute before trying again.";
        } else if (errorMessage.contains('Invalid login credentials')) {
          errorMessage = "Invalid email or password. Please check your inputs.";
        } else if (errorMessage.contains('User already registered')) {
          errorMessage = "This email is already registered. Try logging in instead.";
        } else if (errorMessage.contains('Email not confirmed')) {
          errorMessage = "Please confirm your email in your inbox before logging in.";
        }

        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Row(
              children: [
                const Icon(Icons.error_outline, color: Colors.white),
                const SizedBox(width: 12),
                Expanded(child: Text(errorMessage)),
              ],
            ),
            backgroundColor: Colors.redAccent.withOpacity(0.9),
            behavior: SnackBarBehavior.floating,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
          ),
        );
      } else if (next is AsyncData && previous is AsyncLoading) {
        // Show success message only if transitioning from loading to data (success)
        if (_isRegistering) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      const Icon(Icons.check_circle_outline, color: Colors.white),
                      const SizedBox(width: 12),
                      Text(
                        'Account created!',
                        style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                              color: Colors.white,
                              fontWeight: FontWeight.bold,
                            ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Check your email to confirm your account before logging in.',
                    style: TextStyle(color: Colors.white70),
                  ),
                ],
              ),
              backgroundColor: const Color(0xFF10B981),
              duration: const Duration(seconds: 10),
              behavior: SnackBarBehavior.floating,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            ),
          );
          setState(() {
            _isRegistering = false;
          });
        }
      }
    });

    return Scaffold(
      body: Stack(
        children: [
          // Background Gradient or Image can go here
          Container(
            decoration: const BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topRight,
                end: Alignment.bottomLeft,
                colors: [
                  Color(0xFF0E1511),
                  Color(0xFF0A0F0D),
                ],
              ),
            ),
          ),
          // Glow effects
          Positioned(
            top: -100,
            right: -100,
            child: Container(
              width: 300,
              height: 300,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: const Color(0xFF10B981).withAlpha(50),
                boxShadow: [
                  BoxShadow(
                    color: const Color(0xFF10B981).withAlpha(30),
                    blurRadius: 100,
                    spreadRadius: 50,
                  ),
                ],
              ),
            ),
          ),
          Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(24.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    'ALTERA',
                    style: Theme.of(context).textTheme.displayLarge?.copyWith(
                          letterSpacing: 8,
                          color: const Color(0xFF10B981),
                        ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Your Proactive Second Brain',
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                          color: Colors.white70,
                          letterSpacing: 1.2,
                        ),
                  ),
                  const SizedBox(height: 48),
                  GlassCard(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          _isRegistering ? 'Join Altera' : 'Login',
                          style: Theme.of(context).textTheme.headlineMedium,
                        ),
                        const SizedBox(height: 24),
                        if (_isRegistering) ...[
                          TextField(
                            controller: _fullNameController,
                            decoration: const InputDecoration(
                              hintText: 'Full Name',
                              prefixIcon: Icon(Icons.person_outline, color: Colors.white54),
                            ),
                            onChanged: (_) => setState(() {}),
                          ),
                          _buildRequirementText(
                            'Min. 3 characters',
                            isValid: _fullNameController.text.length >= 3,
                          ),
                          const SizedBox(height: 16),
                        ],
                        TextField(
                          controller: _emailController,
                          decoration: const InputDecoration(
                            hintText: 'Email Address',
                            prefixIcon: Icon(Icons.email_outlined, color: Colors.white54),
                          ),
                          keyboardType: TextInputType.emailAddress,
                          onChanged: (_) => setState(() {}),
                        ),
                        if (_isRegistering)
                          _buildRequirementText(
                            'Must be a valid email format',
                            isValid: RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(_emailController.text),
                          ),
                        const SizedBox(height: 16),
                        TextField(
                          controller: _passwordController,
                          decoration: const InputDecoration(
                            hintText: 'Password',
                            prefixIcon: Icon(Icons.lock_outline, color: Colors.white54),
                          ),
                          obscureText: true,
                          onChanged: (_) => setState(() {}),
                        ),
                        if (_isRegistering)
                          _buildRequirementText(
                            'Min. 8 characters with letters & numbers',
                            isValid: _passwordController.text.length >= 8 && 
                                    RegExp(r'[a-zA-Z]').hasMatch(_passwordController.text) &&
                                    RegExp(r'[0-9]').hasMatch(_passwordController.text),
                          ),
                        const SizedBox(height: 32),
                        ElevatedButton(
                          onPressed: authState.isLoading
                              ? null
                              : () {
                                  final email = _emailController.text.trim();
                                  final password = _passwordController.text.trim();
                                  final fullName = _fullNameController.text.trim();

                                  if (email.isEmpty || password.isEmpty) {
                                    _showErrorSnackBar('Please fill in all fields');
                                    return;
                                  }

                                  if (_isRegistering) {
                                    if (fullName.length < 3) {
                                      _showErrorSnackBar('Full Name must be at least 3 characters');
                                      return;
                                    }
                                    if (!RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(email)) {
                                      _showErrorSnackBar('Please enter a valid email address');
                                      return;
                                    }
                                    if (password.length < 8 || 
                                        !RegExp(r'[a-zA-Z]').hasMatch(password) ||
                                        !RegExp(r'[0-9]').hasMatch(password)) {
                                      _showErrorSnackBar('Password must be at least 8 characters with letters & numbers');
                                      return;
                                    }

                                    ref.read(authControllerProvider.notifier).signUp(
                                          email,
                                          password,
                                          fullName,
                                        );
                                  } else {
                                    ref.read(authControllerProvider.notifier).signIn(
                                          email,
                                          password,
                                        );
                                  }
                                },
                          child: authState.isLoading
                              ? const SizedBox(
                                  height: 20,
                                  width: 20,
                                  child: CircularProgressIndicator(
                                    strokeWidth: 2,
                                    color: Colors.white,
                                  ),
                                )
                              : Text(_isRegistering ? 'CREATE ACCOUNT' : 'AUTHENTICATE'),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 24),
                  TextButton(
                    onPressed: () {
                      setState(() {
                        _isRegistering = !_isRegistering;
                      });
                    },
                    child: Text(
                      _isRegistering 
                          ? "Already have an account? Login" 
                          : "Don't have an account? Join Altera",
                      style: const TextStyle(color: Colors.white70),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildRequirementText(String text, {required bool isValid}) {
    return Padding(
      padding: const EdgeInsets.only(top: 6, left: 4),
      child: Row(
        children: [
          Icon(
            isValid ? Icons.check_circle : Icons.circle_outlined,
            size: 12,
            color: isValid ? const Color(0xFF10B981) : Colors.white38,
          ),
          const SizedBox(width: 6),
          Text(
            text,
            style: TextStyle(
              fontSize: 11,
              color: isValid ? const Color(0xFF10B981) : Colors.white38,
            ),
          ),
        ],
      ),
    );
  }

  void _showErrorSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: Colors.orangeAccent.withOpacity(0.9),
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      ),
    );
  }
}
