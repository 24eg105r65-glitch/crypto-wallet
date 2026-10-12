import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Shield, Lock, User, ArrowRight, Eye, EyeOff, KeyRound, Sparkles, AlertCircle } from 'lucide-react';

export default function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [username, setUsername] = useState('pushkaran');
  const [password, setPassword] = useState('••••••••••••');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!username.trim()) {
      setError('Username or institutional identifier is required');
      return;
    }
    setLoading(true);
    try {
      login(username.trim(), password);
      navigate('/dashboard');
    } catch (err) {
      setError('Authentication failed. Check credentials.');
      setLoading(false);
    }
  };

  const handleQuickDemo = (demoUser) => {
    setUsername(demoUser);
    setPassword('demoSecureVaultPass2026');
    setLoading(true);
    setTimeout(() => {
      login(demoUser, 'demoSecureVaultPass2026');
      navigate('/dashboard');
    }, 150);
  };

  return (
    <div className="min-h-screen bg-[#0B0D0C] text-[#F5F7F5] flex flex-col justify-between selection:bg-[#79E6A0] selection:text-black">
      {/* Top Bar */}
      <header className="px-6 py-6 border-b border-white/[0.06] flex items-center justify-between max-w-7xl mx-auto w-full">
        <Link to="/" className="flex items-center gap-3 group">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-[#79E6A0]/20 to-[#79E6A0]/5 border border-[#79E6A0]/30 flex items-center justify-center text-[#79E6A0] group-hover:border-[#79E6A0] transition-colors">
            <Shield className="w-5 h-5" />
          </div>
          <div>
            <div className="font-display font-bold text-lg tracking-tight text-white flex items-center gap-2">
              CryptoWallet
              <span className="text-[10px] uppercase font-mono px-2 py-0.5 rounded-full bg-[#79E6A0]/10 text-[#79E6A0] border border-[#79E6A0]/20">
                Institutional
              </span>
            </div>
            <div className="text-[11px] text-[#A0AAA3]">Secure Tier-1 Vault</div>
          </div>
        </Link>
        <Link
          to="/"
          className="text-xs text-[#A0AAA3] hover:text-[#79E6A0] transition-colors flex items-center gap-1.5"
        >
          Return to Overview
          <ArrowRight className="w-3.5 h-3.5" />
        </Link>
      </header>

      {/* Main Login Card */}
      <main className="flex-1 flex items-center justify-center p-4 sm:p-6 my-8">
        <div className="w-full max-w-md bg-[#161B18]/90 border border-white/[0.08] rounded-2xl p-6 sm:p-8 backdrop-blur-xl relative shadow-2xl">
          {/* Subtle Accent Glow */}
          <div className="absolute -top-12 left-1/2 -translate-x-1/2 w-48 h-20 bg-[#79E6A0]/15 blur-3xl pointer-events-none rounded-full" />

          <div className="text-center mb-8">
            <div className="w-12 h-12 rounded-2xl bg-[#79E6A0]/10 border border-[#79E6A0]/30 text-[#79E6A0] flex items-center justify-center mx-auto mb-4">
              <Lock className="w-6 h-6" />
            </div>
            <h1 className="font-display text-2xl font-bold tracking-tight text-white mb-2">
              Sign In to Cold Vault
            </h1>
            <p className="text-xs text-[#A0AAA3] leading-relaxed">
              Air-gapped biometric & cryptographic authentication for high-net-worth reserves.
            </p>
          </div>

          {/* Quick Demo Access Bar */}
          <div className="mb-6 p-3 rounded-xl bg-[#111513] border border-white/[0.05]">
            <div className="text-[11px] font-mono uppercase tracking-wider text-[#79E6A0] flex items-center gap-1.5 mb-2 font-semibold">
              <Sparkles className="w-3.5 h-3.5" />
              1-Click Demo Verification
            </div>
            <div className="grid grid-cols-2 gap-2">
              <button
                type="button"
                id="btn-quick-pushkaran"
                onClick={() => handleQuickDemo('pushkaran')}
                className="text-left p-2 rounded-lg bg-[#161B18] hover:bg-[#1F2622] border border-white/[0.06] hover:border-[#79E6A0]/40 transition-all text-xs"
              >
                <div className="font-semibold text-white">Pushkaran</div>
                <div className="text-[10px] text-[#A0AAA3]">ID: 24EG105R65</div>
              </button>
              <button
                type="button"
                id="btn-quick-admin"
                onClick={() => handleQuickDemo('admin')}
                className="text-left p-2 rounded-lg bg-[#161B18] hover:bg-[#1F2622] border border-white/[0.06] hover:border-[#79E6A0]/40 transition-all text-xs"
              >
                <div className="font-semibold text-white">Security Admin</div>
                <div className="text-[10px] text-[#A0AAA3]">ROLE_ADMIN</div>
              </button>
            </div>
          </div>

          {error && (
            <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/30 text-red-400 text-xs flex items-center gap-2">
              <AlertCircle className="w-4 h-4 shrink-0" />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-medium text-[#A0AAA3] mb-1.5">
                Username or Institutional ID
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-[#707B73]">
                  <User className="w-4 h-4" />
                </div>
                <input
                  id="login-username-input"
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="e.g. pushkaran"
                  className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] focus:ring-1 focus:ring-[#79E6A0] rounded-xl pl-10 pr-4 py-2.5 text-sm text-white placeholder-[#707B73] transition-all outline-none font-mono"
                  required
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between mb-1.5">
                <label className="block text-xs font-medium text-[#A0AAA3]">
                  Hardware Security Passphrase
                </label>
                <span className="text-[11px] text-[#79E6A0] hover:underline cursor-pointer">
                  Recovery Seed
                </span>
              </div>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-[#707B73]">
                  <KeyRound className="w-4 h-4" />
                </div>
                <input
                  id="login-password-input"
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Passphrase"
                  className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] focus:ring-1 focus:ring-[#79E6A0] rounded-xl pl-10 pr-10 py-2.5 text-sm text-white placeholder-[#707B73] transition-all outline-none font-mono"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute inset-y-0 right-0 pr-3.5 flex items-center text-[#707B73] hover:text-[#F5F7F5]"
                >
                  {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            <button
              type="submit"
              id="btn-login-submit"
              disabled={loading}
              className="w-full py-3 px-4 rounded-xl bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-semibold text-sm transition-all shadow-lg hover:shadow-[#79E6A0]/25 flex items-center justify-center gap-2 cursor-pointer disabled:opacity-50"
            >
              {loading ? (
                <>
                  <div className="w-4 h-4 border-2 border-black border-t-transparent rounded-full animate-spin" />
                  <span>Verifying Ledger...</span>
                </>
              ) : (
                <>
                  <span>Authenticate & Open Vault</span>
                  <ArrowRight className="w-4 h-4" />
                </>
              )}
            </button>
          </form>

          <div className="mt-6 pt-5 border-t border-white/[0.06] text-center">
            <p className="text-xs text-[#A0AAA3]">
              Need a new institutional cold account?{' '}
              <Link to="/register" className="text-[#79E6A0] font-semibold hover:underline">
                Create Account
              </Link>
            </p>
          </div>
        </div>
      </main>

      {/* Security Footer */}
      <footer className="px-6 py-4 border-t border-white/[0.06] text-center text-[11px] text-[#707B73] flex flex-col sm:flex-row items-center justify-center gap-3">
        <span>FIPS 140-2 Level 4 HSM Multi-Sign</span>
        <span className="hidden sm:inline">•</span>
        <span>Swiss Banking Secrecy Law Compliant</span>
        <span className="hidden sm:inline">•</span>
        <span>Session Protected: TLS 1.3 / Quantum-Resistant</span>
      </footer>
    </div>
  );
}
