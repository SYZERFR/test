package fr.glasslauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private PackageManager pm;
    private GridLayout appGrid;
    private EditText search;
    private int white = Color.WHITE;

    private int dp(float n) { return (int)(n * getResources().getDisplayMetrics().density + .5f); }

    private GradientDrawable rounded(int color, int stroke, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        if (stroke != 0) d.setStroke(dp(1), stroke);
        return d;
    }

    private TextView label(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        pm = getPackageManager();
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        w.setStatusBarColor(Color.TRANSPARENT);
        w.setNavigationBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= 29) {
            w.setNavigationBarContrastEnforced(false);
            w.setStatusBarContrastEnforced(false);
        }
        createHome();
        populate("");
    }

    private LinearLayout glassPanel(int alpha) {
        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setBackground(rounded(alpha, 0x55FFFFFF, 28));
        return p;
    }

    private void createHome() {
        ScrollView scroller = new ScrollView(this);
        scroller.setFillViewport(true);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(38), dp(22), dp(24));
        scroller.addView(page);
        setContentView(scroller);

        TextView date = label(new SimpleDateFormat("EEEE d MMMM", Locale.getDefault()).format(new Date()), 15, 0xEEFFFFFF, true);
        date.setGravity(Gravity.CENTER);
        page.addView(date);

        TextView time = label(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()), 72, white, false);
        time.setGravity(Gravity.CENTER);
        time.setPadding(0, dp(1), 0, dp(18));
        page.addView(time);

        // iOS-inspired translucent Smart Stack card
        LinearLayout smart = glassPanel(0x553B4258);
        smart.setPadding(dp(18), dp(16), dp(18), dp(16));
        TextView eyebrow = label("MARDI  •  ESPACE PERSONNEL", 10, 0xCCFFFFFF, true);
        eyebrow.setLetterSpacing(.13f);
        smart.addView(eyebrow);
        TextView headline = label("Tout est à sa place.", 21, white, true);
        headline.setPadding(0, dp(8), 0, dp(4));
        smart.addView(headline);
        TextView sub = label("Un écran d’accueil simple et élégant", 13, 0xDDFFFFFF, false);
        smart.addView(sub);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.bottomMargin = dp(18);
        page.addView(smart, sp);

        // Search capsule inspired by iOS Spotlight
        search = new EditText(this);
        search.setSingleLine(true);
        search.setTextColor(white);
        search.setHintTextColor(0xCCFFFFFF);
        search.setHint("⌕   Rechercher");
        search.setTextSize(16);
        search.setPadding(dp(18), 0, dp(18), 0);
        search.setBackground(rounded(0x553D4355, 0x66FFFFFF, 30));
        page.addView(search, new LinearLayout.LayoutParams(-1, dp(52)));
        search.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s,int st,int c,int a) {}
            public void onTextChanged(CharSequence s,int st,int before,int count) { populate(s.toString()); }
            public void afterTextChanged(android.text.Editable e) {}
        });

        LinearLayout heading = new LinearLayout(this);
        heading.setGravity(Gravity.CENTER_VERTICAL);
        heading.setPadding(dp(4), dp(22), dp(4), dp(11));
        TextView apps = label("Bibliothèque", 19, white, true);
        heading.addView(apps);
        TextView all = label("Toutes les apps  ›", 12, 0xDDFFFFFF, false);
        LinearLayout.LayoutParams ap = new LinearLayout.LayoutParams(-2, -2);
        ap.gravity = Gravity.CENTER_VERTICAL;
        ap.leftMargin = dp(10);
        heading.addView(all, ap);
        page.addView(heading);

        LinearLayout appPanel = glassPanel(0x55383D50);
        appPanel.setPadding(dp(10), dp(12), dp(10), dp(12));
        appGrid = new GridLayout(this);
        appGrid.setColumnCount(4);
        appPanel.addView(appGrid);
        page.addView(appPanel);

        // Dock-like glass capsule
        LinearLayout dock = new LinearLayout(this);
        dock.setGravity(Gravity.CENTER);
        dock.setPadding(dp(12), dp(10), dp(12), dp(10));
        dock.setBackground(rounded(0x77565B70, 0x77FFFFFF, 32));
        TextView dockText = label("◉      ◉      ◉      ◉", 24, white, false);
        dockText.setGravity(Gravity.CENTER);
        dock.addView(dockText);
        LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(-1, dp(66));
        dp.topMargin = dp(22);
        page.addView(dock, dp);

        TextView note = label("GLASS 26  •  CONCEPT LAUNCHER", 9, 0xAAFFFFFF, true);
        note.setGravity(Gravity.CENTER);
        note.setLetterSpacing(.16f);
        note.setPadding(0, dp(16), 0, 0);
        page.addView(note);
    }

    private void populate(String query) {
        if (appGrid == null) return;
        appGrid.removeAllViews();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        Collections.sort(apps, (a,b) -> String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString()));
        String q = query.toLowerCase(Locale.getDefault()).trim();
        int found = 0;
        for (ResolveInfo info : apps) {
            String name = info.loadLabel(pm).toString();
            if (!name.toLowerCase(Locale.getDefault()).contains(q)) continue;
            addApp(info, name);
            found++;
        }
        if (found == 0) {
            TextView empty = label("Aucune application trouvée", 14, white, false);
            empty.setPadding(dp(12), dp(18), dp(12), dp(18));
            appGrid.addView(empty);
        }
    }

    private void addApp(ResolveInfo info, String name) {
        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setGravity(Gravity.CENTER);
        tile.setPadding(dp(2), dp(7), dp(2), dp(7));
        ImageView icon = new ImageView(this);
        try { icon.setImageDrawable(info.loadIcon(pm)); } catch (Exception ignored) {}
        tile.addView(icon, new LinearLayout.LayoutParams(dp(50), dp(50)));
        TextView title = label(name, 10, white, false);
        title.setGravity(Gravity.CENTER);
        title.setMaxLines(2);
        title.setPadding(0, dp(6), 0, 0);
        tile.addView(title, new LinearLayout.LayoutParams(-1, dp(32)));
        tile.setOnClickListener(v -> {
            try {
                Intent launch = pm.getLaunchIntentForPackage(info.activityInfo.packageName);
                if (launch != null) startActivity(launch);
            } catch (Exception ignored) {}
        });
        GridLayout.LayoutParams gp = new GridLayout.LayoutParams();
        gp.width = 0;
        gp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        appGrid.addView(tile, gp);
    }

    @Override public void onBackPressed() {
        if (search != null && search.length() > 0) search.setText("");
        else super.onBackPressed();
    }
}
