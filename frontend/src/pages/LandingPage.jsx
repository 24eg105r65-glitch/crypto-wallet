import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useVault } from '../context/VaultContext';

export default function LandingPage() {
  const { user } = useAuth();
  const { valuation } = useVault();

  return (
    <div className="min-h-screen bg-[#0B0D0C] text-[#F5F7F5] selection:bg-[#79E6A0]/20 selection:text-[#79E6A0]">
      {/* Sticky Navigation Bar */}
      <header className="sticky top-0 z-40 bg-[#0B0D0C]/85 backdrop-blur-xl border-b border-white/5 py-4 px-6 md:px-12">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <Link to="/" className="flex items-center gap-3 group">
            <div className="w-9 h-9 rounded-xl bg-[#79E6A0]/10 border border-[#79E6A0]/25 flex items-center justify-center text-[#79E6A0] shadow-[0_0_15px_rgba(121,230,160,0.15)] group-hover:scale-105 transition-transform">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#79E6A0" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M19 7V4a1 1 0 0 0-1-1H5a2 2 0 0 0 0 4h15a1 1 0 0 1 1 1v4h-3a2 2 0 0 0 0 4h3a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1" />
                <circle cx="17" cy="14" r="1" fill="#79E6A0" />
              </svg>
            </div>
            <div>
              <span className="font-display font-bold text-lg text-[#F5F7F5] group-hover:text-[#79E6A0] transition-colors">CryptoWallet</span>
              <span className="text-[10px] ml-2 font-mono bg-[#79E6A0]/15 text-[#79E6A0] px-1.5 py-0.5 rounded font-bold">PRO</span>
            </div>
          </Link>

          <nav className="hidden md:flex items-center gap-8 text-sm text-[#A0AAA3]">
            <a href="#features" className="hover:text-[#F5F7F5] transition-colors">Features</a>
            <a href="#vault" className="hover:text-[#F5F7F5] transition-colors">Institutional Vault</a>
            <a href="#security" className="hover:text-[#F5F7F5] transition-colors">Security</a>
            <a href="#pricing" className="hover:text-[#F5F7F5] transition-colors">Pricing</a>
          </nav>

          <div className="flex items-center gap-3">
            {user ? (
              <div className="flex items-center gap-3">
                <Link
                  to="/dashboard"
                  className="bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-display font-bold text-sm px-5 py-2.5 rounded-xl transition-all shadow-md flex items-center gap-2"
                >
                  <span className="material-symbols-outlined text-[18px]">space_dashboard</span>
                  <span>Open Vault</span>
                </Link>
                <Link
                  to="/logout"
                  className="text-xs text-red-400 hover:text-red-300 font-mono px-2 py-1"
                >
                  Sign Out
                </Link>
              </div>
            ) : (
              <div className="flex items-center gap-3">
                <Link
                  to="/login"
                  className="text-sm font-medium text-[#A0AAA3] hover:text-[#F5F7F5] px-3 py-2 transition-colors"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-display font-bold text-sm px-5 py-2.5 rounded-xl transition-all shadow-md"
                >
                  Get Started
                </Link>
              </div>
            )}
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="relative pt-20 pb-28 px-6 md:px-12 overflow-hidden">
        <div className="absolute top-1/4 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[700px] h-[500px] bg-[#79E6A0]/5 rounded-full blur-[140px] pointer-events-none"></div>

        <div className="max-w-6xl mx-auto text-center relative z-10 flex flex-col items-center">
          <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-[#161B18] border border-white/10 text-xs font-mono text-[#79E6A0] mb-8 shadow-sm">
            <span className="w-2 h-2 rounded-full bg-[#79E6A0] animate-pulse"></span>
            <span>NEXT-GEN WEALTH ARCHITECTURE • V2.5 ONLINE</span>
          </div>

          <h1 className="font-display font-extrabold text-4xl sm:text-6xl lg:text-7xl tracking-tight text-[#F5F7F5] max-w-4xl leading-[1.08] mb-6">
            Know exactly where your money goes.
          </h1>

          <p className="text-lg sm:text-xl text-[#A0AAA3] max-w-2xl font-light mb-10 leading-relaxed">
            The ultra-secure, institutional-grade financial command center designed for modern wealth, cold-storage asset vaults, and automated expense intelligence.
          </p>

          <div className="flex flex-col sm:flex-row items-center gap-4 w-full sm:w-auto">
            <Link
              to="/dashboard"
              className="w-full sm:w-auto bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-display font-bold text-base px-8 py-4 rounded-xl transition-all shadow-xl hover:scale-105 active:scale-95 flex items-center justify-center gap-2"
            >
              <span>Launch Pushkaran Vault</span>
              <span className="material-symbols-outlined text-[18px]">arrow_forward</span>
            </Link>
            <Link
              to="/login"
              className="w-full sm:w-auto bg-[#161B18] hover:bg-[#1F2622] text-[#F5F7F5] font-display font-medium text-base px-8 py-4 rounded-xl border border-white/10 transition-all flex items-center justify-center gap-2"
            >
              <span>Access Client Portal</span>
            </Link>
          </div>

          {/* Interactive Hero Terminal Preview */}
          <div className="w-full max-w-4xl mt-16 bg-[#161B18] border border-white/10 rounded-2xl p-6 sm:p-8 shadow-2xl relative overflow-hidden text-left">
            <div className="flex items-center justify-between border-b border-white/10 pb-4 mb-6">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 rounded-full bg-red-500/80"></span>
                <span className="w-3 h-3 rounded-full bg-yellow-500/80"></span>
                <span className="w-3 h-3 rounded-full bg-green-500/80"></span>
                <span className="text-xs font-mono text-[#A0AAA3] ml-2">gotthard-node-vii.zurich.custody</span>
              </div>
              <span className="text-xs font-mono text-[#79E6A0] bg-[#79E6A0]/10 px-2 py-0.5 rounded border border-[#79E6A0]/20">
                HSM FIPS 140-3 LIVE
              </span>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="bg-[#111513] p-5 rounded-xl border border-white/5 flex flex-col justify-between">
                <div>
                  <span className="text-xs font-mono text-[#A0AAA3]">AGGREGATE VALUATION</span>
                  <div className="font-display font-bold text-2xl text-[#F5F7F5] mt-1">
                    ${valuation.totalUsd.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </div>
                  <span className="text-xs font-mono text-[#79E6A0] mt-1 inline-block">+3.42% 30D DELTA</span>
                </div>
                <div className="pt-4 text-[11px] text-[#A0AAA3] font-mono border-t border-white/5 mt-4">
                  Eqv. CHF {valuation.totalChf.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </div>
              </div>

              <div className="bg-[#111513] p-5 rounded-xl border border-white/5 flex flex-col justify-between">
                <div>
                  <span className="text-xs font-mono text-[#A0AAA3]">SWISS ALPINE COLD RESERVE</span>
                  <div className="font-display font-bold text-2xl text-[#79E6A0] mt-1">
                    {valuation.coldPct.toFixed(1)}%
                  </div>
                  <span className="text-xs font-mono text-[#A0AAA3] mt-1 inline-block">Deep-Rock Granite Bunker</span>
                </div>
                <div className="pt-4 text-[11px] text-[#A0AAA3] font-mono border-t border-white/5 mt-4">
                  ${valuation.coldUsd.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </div>
              </div>

              <div className="bg-[#111513] p-5 rounded-xl border border-white/5 flex flex-col justify-between">
                <div>
                  <span className="text-xs font-mono text-[#A0AAA3]">TRUSTEE MULTI-SIG QUORUM</span>
                  <div className="font-display font-bold text-2xl text-[#14F195] mt-1">
                    4 of 7 Keys
                  </div>
                  <span className="text-xs font-mono text-[#A0AAA3] mt-1 inline-block">Zurich • Vaduz • Geneva</span>
                </div>
                <div className="pt-4 text-[11px] text-[#A0AAA3] font-mono border-t border-white/5 mt-4">
                  Hardware NitroKey Attested
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Feature Pillar Grid */}
      <section id="features" className="py-24 px-6 md:px-12 border-t border-white/5 bg-[#111513]">
        <div className="max-w-6xl mx-auto">
          <div className="text-center max-w-2xl mx-auto mb-16">
            <span className="text-xs font-mono text-[#79E6A0] uppercase tracking-wider font-bold">ARCHITECTED FOR PRECISION</span>
            <h2 className="font-display font-bold text-3xl sm:text-4xl text-[#F5F7F5] mt-2 mb-4">
              Complete Sovereignty Over Your Wealth
            </h2>
            <p className="text-[#A0AAA3] text-sm">
              Combine institutional security with the fluid ergonomics of personal money management.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="bg-[#161B18] p-8 rounded-2xl border border-white/5 hover:border-[#79E6A0]/30 transition-all group">
              <div className="w-12 h-12 rounded-xl bg-[#79E6A0]/10 border border-[#79E6A0]/20 flex items-center justify-center text-[#79E6A0] mb-6 group-hover:scale-110 transition-transform">
                <span className="material-symbols-outlined text-[24px]">account_balance</span>
              </div>
              <h3 className="font-display font-bold text-xl text-[#F5F7F5] mb-2">Private Reserve Ledger</h3>
              <p className="text-sm text-[#A0AAA3] leading-relaxed">
                Live valuations across Bitcoin, Ethereum, Solana, and USDT with continuous oracle indexing and local offline caching.
              </p>
            </div>

            <div className="bg-[#161B18] p-8 rounded-2xl border border-white/5 hover:border-[#79E6A0]/30 transition-all group">
              <div className="w-12 h-12 rounded-xl bg-[#79E6A0]/10 border border-[#79E6A0]/20 flex items-center justify-center text-[#79E6A0] mb-6 group-hover:scale-110 transition-transform">
                <span className="material-symbols-outlined text-[24px]">timelapse</span>
              </div>
              <h3 className="font-display font-bold text-xl text-[#F5F7F5] mb-2">Time-Lock Depository</h3>
              <p className="text-sm text-[#A0AAA3] leading-relaxed">
                Air-gapped smart-contract vaults enforcing strict unbonding periods, 14-day delays, and dual-quorum withdrawal windows.
              </p>
            </div>

            <div className="bg-[#161B18] p-8 rounded-2xl border border-white/5 hover:border-[#79E6A0]/30 transition-all group">
              <div className="w-12 h-12 rounded-xl bg-[#79E6A0]/10 border border-[#79E6A0]/20 flex items-center justify-center text-[#79E6A0] mb-6 group-hover:scale-110 transition-transform">
                <span className="material-symbols-outlined text-[24px]">verified</span>
              </div>
              <h3 className="font-display font-bold text-xl text-[#F5F7F5] mb-2">Institutional Audit Proof</h3>
              <p className="text-sm text-[#A0AAA3] leading-relaxed">
                Swiss FINMA Circular 2018/3 digital custody compliance with real Merkle tree inclusion verification and one-click CSV manifests.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="py-12 px-6 md:px-12 border-t border-white/10 text-center text-xs text-[#707B73] font-mono">
        <div className="max-w-6xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center gap-2">
            <span className="font-bold text-[#F5F7F5]">CryptoWallet</span>
            <span>• Built by Adabala Pushkaran (24EG105R65)</span>
          </div>
          <div className="flex items-center gap-6">
            <Link to="/dashboard" className="hover:text-[#79E6A0] transition-colors">Dashboard</Link>
            <Link to="/login" className="hover:text-[#79E6A0] transition-colors">Sign In</Link>
            <Link to="/register" className="hover:text-[#79E6A0] transition-colors">Register</Link>
            <a href="https://github.com/24eg105r65-glitch/crypto-wallet" target="_blank" rel="noreferrer" className="hover:text-[#79E6A0] transition-colors">GitHub</a>
          </div>
        </div>
      </footer>
    </div>
  );
}
