package com.bumptech.glide.load.engine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.c;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GlideException extends Exception {
    private static final long serialVersionUID = 1;
    private static final StackTraceElement[] zq = new StackTraceElement[0];
    private final List<Throwable> causes;
    private Class<?> dataClass;
    private DataSource dataSource;
    private String detailMessage;
    private c key;

    private static final class a implements Appendable {
        private static final String INDENT = "  ";
        private static final String Nf = "";
        private boolean Mf = true;
        private final Appendable appendable;

        a(Appendable appendable2) {
            this.appendable = appendable2;
        }

        @NonNull
        private CharSequence a(@Nullable CharSequence charSequence) {
            return charSequence == null ? "" : charSequence;
        }

        public Appendable append(char c2) throws IOException {
            boolean z = false;
            if (this.Mf) {
                this.Mf = false;
                this.appendable.append(INDENT);
            }
            if (c2 == 10) {
                z = true;
            }
            this.Mf = z;
            this.appendable.append(c2);
            return this;
        }

        public Appendable append(@Nullable CharSequence charSequence) throws IOException {
            CharSequence a2 = a(charSequence);
            return append(a2, 0, a2.length());
        }

        public Appendable append(@Nullable CharSequence charSequence, int i, int i2) throws IOException {
            CharSequence a2 = a(charSequence);
            boolean z = false;
            if (this.Mf) {
                this.Mf = false;
                this.appendable.append(INDENT);
            }
            if (a2.length() > 0 && a2.charAt(i2 - 1) == 10) {
                z = true;
            }
            this.Mf = z;
            this.appendable.append(a2, i, i2);
            return this;
        }
    }

    public GlideException(String str) {
        this(str, Collections.emptyList());
    }

    public GlideException(String str, Throwable th) {
        this(str, Collections.singletonList(th));
    }

    public GlideException(String str, List<Throwable> list) {
        this.detailMessage = str;
        setStackTrace(zq);
        this.causes = list;
    }

    private void a(Appendable appendable) {
        a((Throwable) this, appendable);
        a(Lj(), (Appendable) new a(appendable));
    }

    private static void a(Throwable th, Appendable appendable) {
        try {
            appendable.append(th.getClass().toString()).append(": ").append(th.getMessage()).append(10);
        } catch (IOException unused) {
            throw new RuntimeException(th);
        }
    }

    private void a(Throwable th, List<Throwable> list) {
        if (th instanceof GlideException) {
            for (Throwable a2 : ((GlideException) th).Lj()) {
                a(a2, list);
            }
            return;
        }
        list.add(th);
    }

    private static void a(List<Throwable> list, Appendable appendable) {
        try {
            b(list, appendable);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private static void b(List<Throwable> list, Appendable appendable) throws IOException {
        int size = list.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            appendable.append("Cause (").append(String.valueOf(i2)).append(" of ").append(String.valueOf(size)).append("): ");
            Throwable th = (Throwable) list.get(i);
            if (th instanceof GlideException) {
                ((GlideException) th).a(appendable);
            } else {
                a(th, appendable);
            }
            i = i2;
        }
    }

    public List<Throwable> Lj() {
        return this.causes;
    }

    public List<Throwable> Mj() {
        ArrayList arrayList = new ArrayList();
        a((Throwable) this, (List<Throwable>) arrayList);
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void a(c cVar, DataSource dataSource2) {
        a(cVar, dataSource2, null);
    }

    /* access modifiers changed from: 0000 */
    public void a(c cVar, DataSource dataSource2, Class<?> cls) {
        this.key = cVar;
        this.dataSource = dataSource2;
        this.dataClass = cls;
    }

    public Throwable fillInStackTrace() {
        return this;
    }

    public String getMessage() {
        String str;
        String str2;
        StringBuilder sb = new StringBuilder(71);
        sb.append(this.detailMessage);
        String str3 = ", ";
        String str4 = "";
        if (this.dataClass != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str3);
            sb2.append(this.dataClass);
            str = sb2.toString();
        } else {
            str = str4;
        }
        sb.append(str);
        if (this.dataSource != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str3);
            sb3.append(this.dataSource);
            str2 = sb3.toString();
        } else {
            str2 = str4;
        }
        sb.append(str2);
        if (this.key != null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append(this.key);
            str4 = sb4.toString();
        }
        sb.append(str4);
        List<Throwable> Mj = Mj();
        if (Mj.isEmpty()) {
            return sb.toString();
        }
        if (Mj.size() == 1) {
            sb.append("\nThere was 1 cause:");
        } else {
            sb.append("\nThere were ");
            sb.append(Mj.size());
            sb.append(" causes:");
        }
        for (Throwable th : Mj) {
            sb.append(10);
            sb.append(th.getClass().getName());
            sb.append('(');
            sb.append(th.getMessage());
            sb.append(')');
        }
        sb.append("\n call GlideException#logRootCauses(String) for more detail");
        return sb.toString();
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream printStream) {
        a(printStream);
    }

    public void printStackTrace(PrintWriter printWriter) {
        a(printWriter);
    }

    public void x(String str) {
        List Mj = Mj();
        int size = Mj.size();
        int i = 0;
        while (i < size) {
            StringBuilder sb = new StringBuilder();
            sb.append("Root cause (");
            int i2 = i + 1;
            sb.append(i2);
            sb.append(" of ");
            sb.append(size);
            sb.append(")");
            Log.i(str, sb.toString(), (Throwable) Mj.get(i));
            i = i2;
        }
    }
}
