import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'core/theme/app_theme.dart';
import 'presentation/screens/splash_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await Supabase.initialize(
    url: 'https://efgxgereazfjihmgip.supabase.co',
    anonKey: 'sb_publishable_bYPvixNSZTqS214bTzjyxQ_k9r6kgz0',
  );

  runApp(const ProviderScope(child: AlteraApp()));
}

class AlteraApp extends ConsumerWidget {
  const AlteraApp({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    return MaterialApp(
      title: 'Altera',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      darkTheme: AppTheme.darkTheme,
      themeMode: ThemeMode.system, // Ikut sistem HP user
      home: const SplashScreen(),
    );
  }
}
