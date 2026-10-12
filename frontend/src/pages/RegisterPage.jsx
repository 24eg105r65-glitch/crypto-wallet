import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Shield, Lock, User, Mail, Globe, ArrowRight, CheckCircle2, Sparkles, AlertCircle } from 'lucide-react';

export default function RegisterPage() {
  const navigate = useNavigate();
  const { register } = useAuth();
  
  const [formData, setFormData] = useState({
    fullName: 'Adabala Pushkaran',
    username: 'pushkaran',
    email: 'pushkaran@cryptowallet.io',
    jurisdiction: 'Zurich (CH-ZH)',
    password: 'DemoPassphrase2026!',
    confirmPassword: 'DemoPassphrase2026!'
  });

  const [agreed, setAgreed] = useState(true);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handlePrefillPushkaran = () => {
    setFormData({
      fullName: 'Adabala Pushkaran',
      username: 'pushkaran',
      email: 'pushkaran@cryptowallet.io',
      jurisdiction: 'Zurich (CH-ZH)',
      password: 'DemoPassphrase2026!',
      confirmPassword: 'DemoPassphrase2026!'
    });
    setAgreed(true);
    setError('');
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.fullName.trim() || !formData.username.trim() || !formData.email.trim()) {
      setError('Please provide all mandatory institutional identity credentials');
      return;
    }
    if (formData.password !== formData.confirmPassword) {
      setError('Master Passphrases do not match');
      return;
    }
    if (!agreed) {
      setError('You must accept the Swiss Digital Banking Master Escrow Terms');
      return;
    }

    setLoading(true);
    try {
      register({
        fullName: formData.fullName,
        username: formData.username,
        email: formData.email,
        jurisdiction: formData.jurisdiction
      });
      navigate('/dashboard');
    } catch (err) {
      setError('Registration failed. Please re-check identity parameters.');
      setLoading(false);
    }
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
            <div className="text-[11px] text-[#A0AAA3]">Account Provisioning</div>
          </div>
        </Link>
        <Link
          to="/login"
          className="text-xs text-[#A0AAA3] hover:text-[#79E6A0] transition-colors flex items-center gap-1.5"
        >
          Already have an account? Sign In
          <ArrowRight className="w-3.5 h-3.5" />
        </Link>
      </header>

      {/* Main Registration Card */}
      <main className="flex-1 flex items-center justify-center p-4 sm:p-6 my-8">
        <div className="w-full max-w-lg bg-[#161B18]/90 border border-white/[0.08] rounded-2xl p-6 sm:p-8 backdrop-blur-xl relative shadow-2xl">
          <div className="text-center mb-6">
            <h1 className="font-display text-2xl font-bold tracking-tight text-white mb-2">
              Initialize Institutional Vault
            </h1>
            <p className="text-xs text-[#A0AAA3] leading-relaxed">
              Create an air-gapped cryptographic repository with non-custodial multi-party governance.
            </p>
          </div>

          {/* Quick Prefill Pill */}
          <div className="mb-6 flex justify-center">
            <button
              type="button"
              id="btn-prefill-pushkaran"
              onClick={handlePrefillPushkaran}
              className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full bg-[#79E6A0]/10 hover:bg-[#79E6A0]/20 border border-[#79E6A0]/30 text-xs font-mono text-[#79E6A0] transition-colors cursor-pointer"
            >
              <Sparkles className="w-3.5 h-3.5" />
              Pre-fill Adabala Pushkaran (24EG105R65)
            </button>
          </div>

          {error && (
            <div className="mb-4 p-3 rounded-xl bg-red-500/10 border border-red-500/30 text-red-400 text-xs flex items-center gap-2">
              <AlertCircle className="w-4 h-4 shrink-0" />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div>
                <label className="block text-xs font-medium text-[#A0AAA3] mb-1.5">
                  Full Legal Name
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#707B73]">
                    <User className="w-3.5 h-3.5" />
                  </div>
                  <input
                    id="register-fullname"
                    type="text"
                    name="fullName"
                    value={formData.fullName}
                    onChange={handleChange}
                    className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] focus:ring-1 focus:ring-[#79E6A0] rounded-xl pl-9 pr-3 py-2 text-xs text-white outline-none font-mono"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-[#A0AAA3] mb-1.5">
                  Vault Username
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#707B73]">
                    <User className="w-3.5 h-3.5" />
                  </div>
                  <input
                    id="register-username"
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] focus:ring-1 focus:ring-[#79E6A0] rounded-xl pl-9 pr-3 py-2 text-xs text-white outline-none font-mono"
                    required
                  />
                </div>
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div>
                <label className="block text-xs font-medium text-[#A0AAA3] mb-1.5">
                  Institutional Email
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#707B73]">
                    <Mail className="w-3.5 h-3.5" />
                  </div>
                  <input
                    id="register-email"
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] focus:ring-1 focus:ring-[#79E6A0] rounded-xl pl-9 pr-3 py-2 text-xs text-white outline-none font-mono"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-[#A0AAA3] mb-1.5">
                  Custody Jurisdiction
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#707B73]">
                    <Globe className="w-3.5 h-3.5" />
                  </div>
                  <select
                    name="jurisdiction"
                    value={formData.jurisdiction}
                    onChange={handleChange}
                    className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] rounded-xl pl-9 pr-3 py-2 text-xs text-white outline-none font-mono appearance-none"
                  >
                    <option value="Zurich (CH-ZH)">Zurich (Switzerland)</option>
                    <option value="Geneva (CH-GE)">Geneva (Switzerland)</option>
                    <option value="Singapore (SG-CEN)">Singapore SG-CEN</option>
                    <option value="Vaduz (LI-FL)">Vaduz (Liechtenstein)</option>
                  </select>
                </div>
              </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div>
                <label className="block text-xs font-medium text-[#A0AAA3] mb-1.5">
                  Master Passphrase
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#707B73]">
                    <Lock className="w-3.5 h-3.5" />
                  </div>
                  <input
                    id="register-password"
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] focus:ring-1 focus:ring-[#79E6A0] rounded-xl pl-9 pr-3 py-2 text-xs text-white outline-none font-mono"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-[#A0AAA3] mb-1.5">
                  Confirm Passphrase
                </label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-[#707B73]">
                    <Lock className="w-3.5 h-3.5" />
                  </div>
                  <input
                    id="register-confirm-password"
                    type="password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    className="w-full bg-[#111513] border border-white/[0.08] focus:border-[#79E6A0] focus:ring-1 focus:ring-[#79E6A0] rounded-xl pl-9 pr-3 py-2 text-xs text-white outline-none font-mono"
                    required
                  />
                </div>
              </div>
            </div>

            <div className="pt-2">
              <label className="flex items-start gap-2.5 text-[11px] text-[#A0AAA3] cursor-pointer">
                <input
                  type="checkbox"
                  checked={agreed}
                  onChange={(e) => setAgreed(e.target.checked)}
                  className="mt-0.5 accent-[#79E6A0] rounded cursor-pointer"
                />
                <span>
                  I agree to the non-custodial multi-sign cryptographic protocols and confirm full sovereign responsibility over private keys.
                </span>
              </label>
            </div>

            <button
              type="submit"
              id="btn-register-submit"
              disabled={loading}
              className="w-full py-3 px-4 rounded-xl bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-semibold text-sm transition-all shadow-lg hover:shadow-[#79E6A0]/25 flex items-center justify-center gap-2 cursor-pointer disabled:opacity-50 mt-4"
            >
              {loading ? (
                <>
                  <div className="w-4 h-4 border-2 border-black border-t-transparent rounded-full animate-spin" />
                  <span>Generating Cold Keys...</span>
                </>
              ) : (
                <>
                  <CheckCircle2 className="w-4 h-4" />
                  <span>Deploy Cold Vault Account</span>
                </>
              )}
            </button>
          </form>

          <div className="mt-6 pt-5 border-t border-white/[0.06] text-center">
            <p className="text-xs text-[#A0AAA3]">
              Already authenticated on another terminal?{' '}
              <Link to="/login" className="text-[#79E6A0] font-semibold hover:underline">
                Sign In
              </Link>
            </p>
          </div>
        </div>
      </main>

      {/* Security Footer */}
      <footer className="px-6 py-4 border-t border-white/[0.06] text-center text-[11px] text-[#707B73]">
        CryptoWallet Cold Storage Protocol • ISO-27001 Certified • Zero Knowledge Architecture
      </footer>
    </div>
  );
}
