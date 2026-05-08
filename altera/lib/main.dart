import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'core/theme/app_theme.dart';
import 'features/auth/presentation/providers/auth_providers.dart';
import 'features/auth/presentation/screens/login_screen.dart';
import 'presentation/screens/home_screen.dart';
import 'presentation/screens/splash_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Supabase.initialize(
    url: 'https://efgxgereeazfjlihmgip.supabase.co',
    anonKey: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVmZ3hnZXJlZWF6ZmpsaWhtZ2lwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgwNTcwODUsImV4cCI6MjA5MzYzMzA4NX0.ofSuiaAv7y2807A_Ifb-t-PWJH0sG_OG6m1hvh2u3EY',
  );

  runApp(const ProviderScope(child: AlteraApp()));
}

class AlteraApp extends ConsumerWidget {
  const AlteraApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final authState = ref.watch(authStateProvider);

    return MaterialApp(
      title: 'Altera',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.darkTheme, // Focus on Obsidian Dark Theme
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.dark,
      home: authState.when(
        data: (user) {
          if (user == null) {
            return const LoginScreen();
          }
          return const HomeScreen();
        },
        loading: () => const SplashScreen(),
        error: (err, stack) => Scaffold(
          body: Center(child: Text('Error: $err')),
        ),
      ),
    );
  }
}
