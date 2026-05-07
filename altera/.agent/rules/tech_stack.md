# Altera Tech Stack & Coding Standards

## 1. Architecture: Clean Architecture
Semua kode harus dipisah ke dalam layer:
*   **Data:** Repositories, Data Sources, Models (DTO).
*   **Domain:** Entities, Use Cases, Repository Interfaces.
*   **Presentation:** UI Screens, Widgets, Riverpod Providers.

## 2. State Management: Riverpod
*   Gunakan `flutter_riverpod`.
*   Priority: `AsyncNotifierProvider` untuk data asinkron (Supabase/API).
*   Pisahkan logic (Provider) dari UI (Widget).

## 3. Styling & UI
*   **Font:** Outfit (Google Fonts).
*   **Theme:** Material 3 dengan skema warna Deep Indigo & Emerald.
*   **Aesthetics:** Gunakan Glassmorphism, Premium Gradients, dan micro-animations.

## 4. Database: Supabase
*   Ikuti prinsip Relational Database (SQL).
*   Gunakan RLS (Row Level Security) untuk menjaga privasi data user.
