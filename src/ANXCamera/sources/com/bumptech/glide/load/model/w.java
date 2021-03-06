package com.bumptech.glide.load.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools.Pool;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.a.d;
import com.bumptech.glide.load.c;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.g;
import com.bumptech.glide.util.i;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* compiled from: MultiModelLoader */
class w<Model, Data> implements t<Model, Data> {
    private final Pool<List<Throwable>> gi;
    private final List<t<Model, Data>> xe;

    /* compiled from: MultiModelLoader */
    static class a<Data> implements d<Data>, com.bumptech.glide.load.a.d.a<Data> {
        private com.bumptech.glide.load.a.d.a<? super Data> callback;
        private int currentIndex = 0;
        @Nullable
        private List<Throwable> exceptions;
        private final List<d<Data>> fi;
        private final Pool<List<Throwable>> lc;
        private Priority priority;

        a(@NonNull List<d<Data>> list, @NonNull Pool<List<Throwable>> pool) {
            this.lc = pool;
            i.a(list);
            this.fi = list;
        }

        private void Ek() {
            if (this.currentIndex < this.fi.size() - 1) {
                this.currentIndex++;
                a(this.priority, this.callback);
                return;
            }
            i.checkNotNull(this.exceptions);
            this.callback.b((Exception) new GlideException("Fetch failed", (List<Throwable>) new ArrayList<Throwable>(this.exceptions)));
        }

        @NonNull
        public Class<Data> M() {
            return ((d) this.fi.get(0)).M();
        }

        public void a(@NonNull Priority priority2, @NonNull com.bumptech.glide.load.a.d.a<? super Data> aVar) {
            this.priority = priority2;
            this.callback = aVar;
            this.exceptions = (List) this.lc.acquire();
            ((d) this.fi.get(this.currentIndex)).a(priority2, this);
        }

        public void b(@NonNull Exception exc) {
            List<Throwable> list = this.exceptions;
            i.checkNotNull(list);
            list.add(exc);
            Ek();
        }

        public void b(@Nullable Data data) {
            if (data != null) {
                this.callback.b(data);
            } else {
                Ek();
            }
        }

        public void cancel() {
            for (d cancel : this.fi) {
                cancel.cancel();
            }
        }

        public void cleanup() {
            List<Throwable> list = this.exceptions;
            if (list != null) {
                this.lc.release(list);
            }
            this.exceptions = null;
            for (d cleanup : this.fi) {
                cleanup.cleanup();
            }
        }

        @NonNull
        public DataSource r() {
            return ((d) this.fi.get(0)).r();
        }
    }

    w(@NonNull List<t<Model, Data>> list, @NonNull Pool<List<Throwable>> pool) {
        this.xe = list;
        this.gi = pool;
    }

    public com.bumptech.glide.load.model.t.a<Data> a(@NonNull Model model, int i, int i2, @NonNull g gVar) {
        int size = this.xe.size();
        ArrayList arrayList = new ArrayList(size);
        c cVar = null;
        for (int i3 = 0; i3 < size; i3++) {
            t tVar = (t) this.xe.get(i3);
            if (tVar.c(model)) {
                com.bumptech.glide.load.model.t.a a2 = tVar.a(model, i, i2, gVar);
                if (a2 != null) {
                    cVar = a2.we;
                    arrayList.add(a2.bi);
                }
            }
        }
        if (arrayList.isEmpty() || cVar == null) {
            return null;
        }
        return new com.bumptech.glide.load.model.t.a<>(cVar, new a(arrayList, this.gi));
    }

    public boolean c(@NonNull Model model) {
        for (t c2 : this.xe) {
            if (c2.c(model)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MultiModelLoader{modelLoaders=");
        sb.append(Arrays.toString(this.xe.toArray()));
        sb.append('}');
        return sb.toString();
    }
}
