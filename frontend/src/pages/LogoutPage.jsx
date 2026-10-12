import React, { useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { ShieldCheck, LogIn, Home, Lock, CheckCircle2 } from 'lucide-react';

export default function LogoutPage() {
  const navigate = useNavigate();
  const { logout } = useAuth();

  useEffect(() => {
    // Instantly wipe credentials and active tokens upon rendering
    logout();
  }, [logout]);

  return (
    <div className="min-h-screen bg-[#0B0D0C] text-[#F5F7F5] flex flex-col justify-between selection:bg-[#79E6A0] selection:text-black">
      {/* Top Bar */}
      <header className="px-6 py-6 border-b border-white/[0.06] flex items-center justify-between max-w-7xl mx-auto w-full">
        <Link to="/" className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-[#79E6A0]/20 to-[#79E6A0]/5 border border-[#79E6A0]/30 flex items-center justify-center text-[#79E6A0]">
            <Lock className="w-5 h-5" />
          </div>
          <div>
            <div className="font-display font-bold text-lg tracking-tight text-white flex items-center gap-2">
              CryptoWallet
            </div>
            <div className="text-[11px] text-[#A0AAA3]">Vault Terminal</div>
          </div>
        </Link>
        <Link
          to="/"
          className="text-xs text-[#A0AAA3] hover:text-[#79E6A0] transition-colors flex items-center gap-1.5"
        >
          <Home className="w-3.5 h-3.5" />
          Landing Page
        </Link>
      </header>

      {/* Main Logout Card */}
      <main className="flex-1 flex items-center justify-center p-4 sm:p-6 my-8">
        <div className="w-full max-w-md bg-[#161B18]/90 border border-white/[0.08] rounded-2xl p-6 sm:p-8 backdrop-blur-xl relative shadow-2xl text-center">
          {/* Subtle Glow */}
          <div className="absolute -top-10 left-1/2 -translate-x-1/2 w-40 h-20 bg-[#79E6A0]/10 blur-3xl pointer-events-none rounded-full" />

          <div className="w-16 h-16 rounded-2xl bg-[#79E6A0]/10 border border-[#79E6A0]/30 text-[#79E6A0] flex items-center justify-center mx-auto mb-5 shadow-lg shadow-[#79E6A0]/10">
            <ShieldCheck className="w-8 h-8" />
          </div>

          <h1 className="font-display text-2xl font-bold tracking-tight text-white mb-2">
            Session Terminated
          </h1>
          <p className="text-xs text-[#A0AAA3] leading-relaxed mb-6">
            Your cryptographic keys and ephemeral memory tokens have been safely wiped from this browser. The cold ledger remains intact and air-gapped.
          </p>

          {/* Audit Confirmation checklist */}
          <div className="bg-[#111513] border border-white/[0.05] rounded-xl p-4 text-left space-y-2.5 mb-6 text-xs text-[#A0AAA3] font-mono">
            <div className="flex items-center gap-2 text-emerald-400">
              <CheckCircle2 className="w-3.5 h-3.5 shrink-0" />
              <span>Session tokens invalidated</span>
            </div>
            <div className="flex items-center gap-2 text-emerald-400">
              <CheckCircle2 className="w-3.5 h-3.5 shrink-0" />
              <span>Biometric signature buffer flushed</span>
            </div>
            <div className="flex items-center gap-2 text-emerald-400">
              <CheckCircle2 className="w-3.5 h-3.5 shrink-0" />
              <span>Hardware wallet disconnected</span>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="space-y-3">
            <Link
              to="/login"
              id="btn-logout-relogin"
              className="w-full py-3 px-4 rounded-xl bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-semibold text-sm transition-all shadow-lg hover:shadow-[#79E6A0]/25 flex items-center justify-center gap-2 cursor-pointer"
            >
              <LogIn className="w-4 h-4" />
              <span>Authenticate / Sign In Again</span>
            </Link>

            <Link
              to="/"
              id="btn-logout-home"
              className="w-full py-2.5 px-4 rounded-xl bg-[#1F2622] hover:bg-white/[0.08] text-white border border-white/[0.08] text-xs font-medium transition-all flex items-center justify-center gap-2 cursor-pointer"
            >
              <Home className="w-3.5 h-3.5 text-[#A0AAA3]" />
              <span>Return to Public Overview</span>
            </Link>
          </div>
        </div>
      </main>

      {/* Security Footer */}
      <footer className="px-6 py-4 border-t border-white/[0.06] text-center text-[11px] text-[#707B73]">
        CryptoWallet • Zero Ephemeral Footprint Protocol
      </footer>
    </div>
  );
}
