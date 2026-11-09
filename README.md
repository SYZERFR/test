# Glass 26 Launcher — concept iOS 26

Projet Android natif de launcher inspiré de l'esthétique iOS 26 / Liquid Glass :
- grandes horloge/date centrées
- cartes translucides arrondies
- capsule de recherche type Spotlight
- grille d'applications
- dock vitré décoratif
- fond d'écran système visible en arrière-plan

## Compiler
Ouvrir le dossier dans Android Studio, synchroniser Gradle, puis Build > Build APK(s).
APK debug : `app/build/outputs/apk/debug/app-debug.apk`.

Installer l'APK, puis sélectionner Glass Launcher dans les paramètres Android comme application d'accueil.

## Limites
Ceci est un launcher tiers : il ne remplace pas le centre de contrôle, les notifications, l'écran verrouillé ou les réglages HyperOS. Le dock est décoratif dans cette version. L'effet Liquid Glass est une approximation visuelle Android, pas l'effet natif Apple.

## Construire depuis un téléphone Android (sans PC)
Le dossier contient un workflow GitHub Actions qui compile l'APK dans le cloud.

1. Connecte-toi à github.com dans Chrome et crée un dépôt (repository) vide.
2. Dans le dépôt, utilise **Add file > Upload files** et téléverse le contenu du ZIP (y compris le dossier `.github/workflows`).
3. Ouvre l'onglet **Actions** et sélectionne **Build Glass 26 APK** si le workflow n'a pas démarré automatiquement.
4. Lance **Run workflow** puis attends la fin du travail.
5. Ouvre l'exécution terminée, descends jusqu'à **Artifacts** et télécharge **Glass26-debug-apk**.
6. Décompresse l'archive téléchargée : elle contient `app-debug.apk`. Ouvre-le pour installer l'application.

Il faut un compte GitHub. Le fichier généré est un APK de débogage non signé pour publication, mais installable manuellement sur Android.
