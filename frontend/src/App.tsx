import { Navigate, Route, Routes } from 'react-router-dom';
import { UserRole } from './types/domain';
import { DashboardRedirect } from './pages/DashboardRedirect';
import { HomePage } from './pages/HomePage';
import { LoginPage } from './pages/LoginPage';
import { LicenseVerificationPage } from './pages/LicenseVerificationPage';
import { RegisterPage } from './pages/RegisterPage';
import { CustomerApplicationPage } from './pages/customer/CustomerApplicationPage';
import { CustomerHomePage } from './pages/customer/CustomerHomePage';
import { CustomerLicenseActionPage } from './pages/customer/CustomerLicenseActionPage';
import { ReviewerPage } from './pages/reviewer/ReviewerPage';
import { ApproverPage } from './pages/approver/ApproverPage';
import { AdminPage } from './pages/admin/AdminPage';
import { ProtectedRoute } from './components/routing/ProtectedRoute';
import { AppLayout } from './components/layout/AppLayout';
import { NotFoundPage } from './pages/NotFoundPage';

export function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/verify" element={<LicenseVerificationPage />} />
      <Route
        element={
          <ProtectedRoute>
            <AppLayout />
          </ProtectedRoute>
        }
      >
        <Route path="/dashboard" element={<DashboardRedirect />} />
        <Route
          path="customer"
          element={
            <ProtectedRoute roles={[UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN]}>
              <CustomerHomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="customer/apply"
          element={
            <ProtectedRoute roles={[UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN]}>
              <CustomerApplicationPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="customer/update"
          element={
            <ProtectedRoute roles={[UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN]}>
              <CustomerLicenseActionPage mode="update" />
            </ProtectedRoute>
          }
        />
        <Route
          path="customer/cancel"
          element={
            <ProtectedRoute roles={[UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN]}>
              <CustomerLicenseActionPage mode="cancel" />
            </ProtectedRoute>
          }
        />
        <Route
          path="customer/renew"
          element={
            <ProtectedRoute roles={[UserRole.CUSTOMER, UserRole.LICENSEE, UserRole.ADMIN]}>
              <CustomerLicenseActionPage mode="renew" />
            </ProtectedRoute>
          }
        />
        <Route
          path="reviewer"
          element={
            <ProtectedRoute roles={[UserRole.REVIEWER, UserRole.ADMIN]}>
              <ReviewerPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="approver"
          element={
            <ProtectedRoute roles={[UserRole.APPROVER, UserRole.ADMIN]}>
              <ApproverPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="admin"
          element={
            <ProtectedRoute roles={[UserRole.ADMIN]}>
              <AdminPage />
            </ProtectedRoute>
          }
        />
      </Route>
      <Route path="/403" element={<NotFoundPage forbidden />} />
      <Route path="/404" element={<NotFoundPage />} />
      <Route path="*" element={<Navigate to="/404" replace />} />
    </Routes>
  );
}
