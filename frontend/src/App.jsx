import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { VaultProvider } from './context/VaultContext';
import LandingPage from './pages/LandingPage';
import DashboardPage from './pages/DashboardPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import LogoutPage from './pages/LogoutPage';

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <VaultProvider>
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/dashboard.html" element={<DashboardPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/login.html" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/register.html" element={<RegisterPage />} />
            <Route path="/signup" element={<RegisterPage />} />
            <Route path="/logout" element={<LogoutPage />} />
            <Route path="/logout.html" element={<LogoutPage />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </VaultProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}
