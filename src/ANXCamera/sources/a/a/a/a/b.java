package a.a.a.a;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwBinder.DeathRecipient;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import android.os.statistics.E2EScenario;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/* compiled from: IBase */
public interface b extends IHwInterface {
    public static final String kInterfaceName = "android.hidl.base@1.0::IBase";

    /* compiled from: IBase */
    public static final class a implements b {
        private IHwBinder mRemote;

        public a(IHwBinder iHwBinder) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(iHwBinder);
        }

        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            hwParcel.writeNativeHandle(nativeHandle);
            hwParcel.writeStringVector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256131655, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public a getDebugInfo() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(257049926, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                a aVar = new a();
                aVar.readFromParcel(hwParcel2);
                return aVar;
            } finally {
                hwParcel2.release();
            }
        }

        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256398152, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                ArrayList<byte[]> arrayList = new ArrayList<>();
                HwBlob readBuffer = hwParcel2.readBuffer(16);
                int int32 = readBuffer.getInt32(8);
                HwBlob readEmbeddedBuffer = hwParcel2.readEmbeddedBuffer((long) (int32 * 32), readBuffer.handle(), 0, true);
                arrayList.clear();
                for (int i = 0; i < int32; i++) {
                    byte[] bArr = new byte[32];
                    readEmbeddedBuffer.copyToInt8Array((long) (i * 32), bArr, 32);
                    arrayList.add(bArr);
                }
                return arrayList;
            } finally {
                hwParcel2.release();
            }
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256067662, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readStringVector();
            } finally {
                hwParcel2.release();
            }
        }

        public String interfaceDescriptor() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256136003, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readString();
            } finally {
                hwParcel2.release();
            }
        }

        public boolean linkToDeath(DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }

        public void notifySyspropsChanged() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(257120595, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        public void ping() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256921159, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        public void setHALInstrumentation() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken(b.kInterfaceName);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256462420, hwParcel, hwParcel2, 1);
                hwParcel.releaseTemporaryStorage();
            } finally {
                hwParcel2.release();
            }
        }

        public String toString() {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(interfaceDescriptor());
                sb.append("@Proxy");
                return sb.toString();
            } catch (RemoteException unused) {
                return "[class or subclass of android.hidl.base@1.0::IBase]@Proxy";
            }
        }

        public boolean unlinkToDeath(DeathRecipient deathRecipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(deathRecipient);
        }
    }

    /* renamed from: a.a.a.a.b$b reason: collision with other inner class name */
    /* compiled from: IBase */
    public static abstract class C0001b extends HwBinder implements b {
        public IHwBinder asBinder() {
            return this;
        }

        public void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) {
        }

        public final a getDebugInfo() {
            a aVar = new a();
            aVar.pid = HidlSupport.getPidIfSharable();
            aVar.ptr = 0;
            aVar.arch = 0;
            return aVar;
        }

        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[][]{new byte[]{-20, Byte.MAX_VALUE, -41, -98, -48, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, -13, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}}));
        }

        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(new String[]{b.kInterfaceName}));
        }

        public final String interfaceDescriptor() {
            return b.kInterfaceName;
        }

        public final boolean linkToDeath(DeathRecipient deathRecipient, long j) {
            return true;
        }

        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        public void onTransact(int i, HwParcel hwParcel, HwParcel hwParcel2, int i2) throws RemoteException {
            String str = b.kInterfaceName;
            int i3 = 0;
            boolean z = true;
            switch (i) {
                case 256067662:
                    if ((i2 & 1) == 0) {
                        z = false;
                    }
                    if (z) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    ArrayList interfaceChain = interfaceChain();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeStringVector(interfaceChain);
                    hwParcel2.send();
                    return;
                case 256131655:
                    if ((i2 & 1) == 0) {
                        z = false;
                    }
                    if (z) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    debug(hwParcel.readNativeHandle(), hwParcel.readStringVector());
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 256136003:
                    if ((i2 & 1) == 0) {
                        z = false;
                    }
                    if (z) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    String interfaceDescriptor = interfaceDescriptor();
                    hwParcel2.writeStatus(0);
                    hwParcel2.writeString(interfaceDescriptor);
                    hwParcel2.send();
                    return;
                case 256398152:
                    if ((i2 & 1) == 0) {
                        z = false;
                    }
                    if (z) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    ArrayList hashChain = getHashChain();
                    hwParcel2.writeStatus(0);
                    HwBlob hwBlob = new HwBlob(16);
                    int size = hashChain.size();
                    hwBlob.putInt32(8, size);
                    hwBlob.putBool(12, false);
                    HwBlob hwBlob2 = new HwBlob(size * 32);
                    while (i3 < size) {
                        long j = (long) (i3 * 32);
                        byte[] bArr = (byte[]) hashChain.get(i3);
                        if (bArr == null || bArr.length != 32) {
                            throw new IllegalArgumentException("Array element is not of the expected length");
                        }
                        hwBlob2.putInt8Array(j, bArr);
                        i3++;
                    }
                    hwBlob.putBlob(0, hwBlob2);
                    hwParcel2.writeBuffer(hwBlob);
                    hwParcel2.send();
                    return;
                case 256462420:
                    if ((i2 & 1) != 0) {
                        i3 = 1;
                    }
                    if (i3 != 1) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    setHALInstrumentation();
                    return;
                case 256660548:
                    if ((i2 & 1) != 0) {
                        i3 = 1;
                    }
                    if (i3 != 0) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    return;
                case 256921159:
                    if ((i2 & 1) == 0) {
                        z = false;
                    }
                    if (z) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    ping();
                    hwParcel2.writeStatus(0);
                    hwParcel2.send();
                    return;
                case 257049926:
                    if ((i2 & 1) == 0) {
                        z = false;
                    }
                    if (z) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    a debugInfo = getDebugInfo();
                    hwParcel2.writeStatus(0);
                    debugInfo.writeToParcel(hwParcel2);
                    hwParcel2.send();
                    return;
                case 257120595:
                    if ((i2 & 1) != 0) {
                        i3 = 1;
                    }
                    if (i3 != 1) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    hwParcel.enforceInterface(str);
                    notifySyspropsChanged();
                    return;
                case 257250372:
                    if ((i2 & 1) != 0) {
                        i3 = 1;
                    }
                    if (i3 != 0) {
                        hwParcel2.writeStatus(Integer.MIN_VALUE);
                        hwParcel2.send();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public final void ping() {
        }

        public IHwInterface queryLocalInterface(String str) {
            if (b.kInterfaceName.equals(str)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String str) throws RemoteException {
            registerService(str);
        }

        public final void setHALInstrumentation() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(interfaceDescriptor());
            sb.append("@Stub");
            return sb.toString();
        }

        public final boolean unlinkToDeath(DeathRecipient deathRecipient) {
            return true;
        }
    }

    static b asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        String str = kInterfaceName;
        b queryLocalInterface = iHwBinder.queryLocalInterface(str);
        if (queryLocalInterface != null && (queryLocalInterface instanceof b)) {
            return queryLocalInterface;
        }
        a aVar = new a(iHwBinder);
        try {
            Iterator it = aVar.interfaceChain().iterator();
            while (it.hasNext()) {
                if (((String) it.next()).equals(str)) {
                    return aVar;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    static b castFrom(IHwInterface iHwInterface) {
        if (iHwInterface == null) {
            return null;
        }
        return asInterface(iHwInterface.asBinder());
    }

    static b getService() throws RemoteException {
        return getService(E2EScenario.DEFAULT_CATEGORY);
    }

    static b getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str));
    }

    static b getService(String str, boolean z) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, str, z));
    }

    static b getService(boolean z) throws RemoteException {
        return getService(E2EScenario.DEFAULT_CATEGORY, z);
    }

    IHwBinder asBinder();

    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    a getDebugInfo() throws RemoteException;

    ArrayList<byte[]> getHashChain() throws RemoteException;

    ArrayList<String> interfaceChain() throws RemoteException;

    String interfaceDescriptor() throws RemoteException;

    boolean linkToDeath(DeathRecipient deathRecipient, long j) throws RemoteException;

    void notifySyspropsChanged() throws RemoteException;

    void ping() throws RemoteException;

    void setHALInstrumentation() throws RemoteException;

    boolean unlinkToDeath(DeathRecipient deathRecipient) throws RemoteException;
}
