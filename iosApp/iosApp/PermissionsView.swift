import SwiftUI
import SharedCode

struct PermissionsView: View {
    @State private var cameraStatus: String = "غير معروف"
    @State private var notificationStatus: String = "لم يتم الطلب"
    @State private var galleryStatus: String = "غير معروف"

    var body: some View {
        ScrollView {
            VStack(spacing: 25) {
                Text("إدارة التصاريح")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.top)

                // قسم الإشعارات
                PermissionCard(
                    title: "الإشعارات",
                    status: notificationStatus,
                    icon: "bell.badge.fill",
                    color: .green,
                    action: { requestNotifications() }
                )

                // قسم الكاميرا
                PermissionCard(
                    title: "الكاميرا",
                    status: cameraStatus,
                    icon: "camera.fill",
                    color: .blue,
                    action: { requestCamera() }
                )

                // قسم المعرض
                PermissionCard(
                    title: "معرض الصور",
                    status: galleryStatus,
                    icon: "photo.on.rectangle.angled",
                    color: .purple,
                    action: { requestGallery() }
                )

                Spacer()
            }
            .padding()
        }
        .onAppear {
            requestNotifications()
        }
    }

    // --- الدوال الخاصة بطلب التصاريح ---

    private func requestCamera() {
        Task {
            do {
                let status = try await IOSPermissionManager.shared.requestPermission(permission: CameraPermission())
                updateStatusText(status: status, for: "camera")
                handlePermissionResult(status: status)
            } catch {
                print(error)
            }
        }
    }

    private func requestNotifications() {
        Task {
            do {
                let status = try await IOSPermissionManager.shared.requestPermission(permission: NotificationPermission())
                updateStatusText(status: status, for: "notification")
                handlePermissionResult(status: status)
            } catch {
                print(error)
            }
        }
    }

    private func requestGallery() {
        Task {
            do {
                let status = try await IOSPermissionManager.shared.requestPermission(permission: GalleryPermission())
                updateStatusText(status: status, for: "gallery")
                handlePermissionResult(status: status)
            } catch {
                print(error)
            }
        }
    }

    // دالة لتحديث النصوص المعروضة في الواجهة بالعربي
    private func updateStatusText(status: PermissionStatus, for type: String) {
        let text: String
        switch status {
        case is PermissionStatusGranted:
            text = "مسموح"
        case is PermissionStatusDenied:
            text = "مرفوض"
        case is PermissionStatusDeniedPermanently:
            text = "مرفوض نهائياً"
        case is PermissionStatusUnavailable:
            text = "غير متوفر"
        default:
            text = "غير معروف"
        }

        // تحديث المتغير الصحيح بناءً على النوع
        DispatchQueue.main.async {
            if type == "camera" {
                self.cameraStatus = text
            } else if type == "notification" {
                self.notificationStatus = text
            } else if type == "gallery" {
                self.galleryStatus = text
            }
        }
    }

    private func handlePermissionResult(status: PermissionStatus) {
        if status is PermissionStatusDeniedPermanently {
            IOSPermissionManager.shared.openAppSettings()
        }
    }
}

// مكون فرعي للبطاقة
struct PermissionCard: View {
    let title: String
    let status: String
    let icon: String
    let color: Color
    let action: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Image(systemName: icon)
                    .foregroundColor(color)
                    .font(.headline)
                Text(title)
                    .font(.headline)
                Spacer()
                Text(status)
                    .font(.subheadline)
                    .foregroundColor(status == "مسموح" ? .green : (status == "مرفوض" ? .red : .gray))
            }

            Button(action: action) {
                Label("طلب إذن \(title)", systemImage: icon)
                    .font(.subheadline)
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 12)
                    .background(color)
                    .foregroundColor(.white)
                    .cornerRadius(10)
            }
        }
        .padding()
        .background(Color(.secondarySystemBackground))
        .cornerRadius(12)
    }
}

