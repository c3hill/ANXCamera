.class Lmiui/util/Pools$1;
.super Lmiui/util/Pools$Manager;
.source "Pools.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingClass;
    value = Lmiui/util/Pools;
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Lmiui/util/Pools$Manager<",
        "Ljava/lang/StringBuilder;",
        ">;"
    }
.end annotation


# direct methods
.method constructor <init>()V
    .registers 1

    invoke-direct {p0}, Lmiui/util/Pools$Manager;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic createInstance()Ljava/lang/Object;
    .registers 2

    invoke-virtual {p0}, Lmiui/util/Pools$1;->createInstance()Ljava/lang/StringBuilder;

    move-result-object v0

    return-object v0
.end method

.method public createInstance()Ljava/lang/StringBuilder;
    .registers 2

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    return-object v0
.end method

.method public bridge synthetic onRelease(Ljava/lang/Object;)V
    .registers 2

    check-cast p1, Ljava/lang/StringBuilder;

    invoke-virtual {p0, p1}, Lmiui/util/Pools$1;->onRelease(Ljava/lang/StringBuilder;)V

    return-void
.end method

.method public onRelease(Ljava/lang/StringBuilder;)V
    .registers 3

    const/4 v0, 0x0

    invoke-virtual {p1, v0}, Ljava/lang/StringBuilder;->setLength(I)V

    return-void
.end method
