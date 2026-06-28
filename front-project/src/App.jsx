import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LandingPage from './pages/LandingPage';
import AuthPage from './pages/AuthPage';
import PatientDashboard from './pages/PatientDashboard';
import ProcedureSelection from './pages/ProcedureSelection';
import DoctorList from './pages/DoctorList';
import DoctorProfile from './pages/DoctorProfile';
import TimeSelection from './pages/TimeSelection';
import SuccessPage from './pages/SuccessPage';
import PatientProfile from './pages/PatientProfile';
import Layout from './components/layout/Layout';
import AdminLayout from './components/layout/AdminLayout';
import AdminDashboard from './pages/admin/AdminDashboard';
import DoctorLayout from './components/layout/DoctorLayout';
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import DoctorOwnProfile from './pages/doctor/DoctorProfile';
import DoctorReviews from './pages/doctor/DoctorReviews';

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/auth" element={<AuthPage />} />

        <Route element={<Layout />}>
          <Route path="/patient/dashboard" element={<PatientDashboard />} />
          <Route path="/patient/procedures" element={<ProcedureSelection />} />
          <Route path="/patient/doctors" element={<DoctorList />} />
          <Route path="/patient/doctor/:id" element={<DoctorProfile />} />
          <Route path="/patient/book/:id" element={<TimeSelection />} />
          <Route path="/patient/success" element={<SuccessPage />} />
          <Route path="/patient/profile" element={<PatientProfile />} />
        </Route>

        {/* Admin Routes */}
        <Route element={<AdminLayout />}>
          <Route path="/admin/dashboard" element={<AdminDashboard />} />
          {/* We use dashboard as the primary approval page for now */}
          <Route path="/admin/doctors" element={<AdminDashboard />} />
        </Route>
        
        {/* Doctor Routes */}
        <Route element={<DoctorLayout />}>
          <Route path="/doctor/dashboard" element={<DoctorDashboard />} />
          <Route path="/doctor/profile" element={<DoctorOwnProfile />} />
          <Route path="/doctor/reviews" element={<DoctorReviews />} />
        </Route>
        
        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
