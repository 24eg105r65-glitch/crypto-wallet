import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useVault } from '../context/VaultContext';
import SendModal from '../components/SendModal';
import ReceiveModal from '../components/ReceiveModal';
import FaucetModal from '../components/FaucetModal';
import AdminModal from '../components/AdminModal';
import EpochModal from '../components/EpochModal';

export default function DashboardPage() {
  const { user, logout } = useAuth();
  const { balances, transactions, valuation, resetState, showToast } = useVault();
  const navigate = useNavigate();

  // Navigation & Modals State
  const [activeTab, setActiveTab] = useState('reserve');
  const [isSendOpen, setIsSendOpen] = useState(false);
  const [isReceiveOpen, setIsReceiveOpen] = useState(false);
  const [isFaucetOpen, setIsFaucetOpen] = useState(false);
  const [isAdminOpen, setIsAdminOpen] = useState(false);
  const [isEpochOpen, setIsEpochOpen] = useState(false);
  const [selectedAsset, setSelectedAsset] = useState('BTC');
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const [trusteeCount, setTrusteeCount] = useState(4);

  const handleLogout = (e) => {
    if (e && e.preventDefault) e.preventDefault();
    logout();
    navigate('/logout');
  };

  const handleOpenSend = (asset = 'BTC') => {
    setSelectedAsset(asset);
    setIsSendOpen(true);
  };

  const handleOpenReceive = (asset = 'BTC') => {
    setSelectedAsset(asset);
    setIsReceiveOpen(true);
  };

  const handleSimulateTrustee = () => {
    if (trusteeCount < 7) {
      setTrusteeCount(prev => prev + 1);
      showToast(`Trustee Quorum updated: ${trusteeCount + 1} of 7 Trustees satisfied (Gotthard, Vaduz, Geneva) ✓`, 'teal');
    } else {
      setTrusteeCount(4);
      showToast('Quorum reset to baseline 4 of 7 Trustees.', 'gold');
    }
  };

  const exportCsv = () => {
    const rows = [
      ['CryptoVault Sovereign Custody — Audit Manifest'],
      ['Attestation Date', new Date().toISOString()],
      ['Lead Engineer', user?.fullName || 'Adabala Pushkaran (24EG105R65)'],
      ['Facility', 'Gotthard Subterranean Depository ZUR-04'],
      ['Hardware Enclave', 'FIPS 140-3 Level 4 Alpine Node VII'],
      [''],
      ['Asset', 'Holdings', 'Unit Price USD', 'Total Value USD', 'Custody Status'],
    ];

    for (const key in balances) {
      const a = balances[key];
      rows.push([a.name, a.balance, a.priceUsd, (a.balance * a.priceUsd).toFixed(2), a.status]);
    }

    rows.push(['']);
    rows.push(['Recent Confirmed Blocks']);
    rows.push(['Block #', 'Tx Hash', 'Asset', 'Amount', 'Fee', 'Status']);

    transactions.forEach(t => {
      rows.push([t.block, t.hash, t.asset, t.amount, t.fee, t.status]);
    });

    const csvContent = "data:text/csv;charset=utf-8," + rows.map(e => e.join(",")).join("\n");
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", `auravault_audit_manifest_${Date.now()}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast("Downloaded auravault_audit_manifest.csv", "teal");
  };

  return (
    <div className="min-h-screen bg-[#0B0D0C] text-[#F5F7F5] selection:bg-[#79E6A0]/20 selection:text-[#79E6A0]">
      {/* Fixed Left Lateral Navigation Drawer */}
      <aside className="fixed left-0 top-0 h-full w-72 bg-[#070908] z-50 flex flex-col justify-between py-6 px-5 border-r border-white/5 overflow-y-auto">
        <div className="flex flex-col gap-6">
          {/* Brand Crest */}
          <div>
            <Link to="/" className="flex items-center gap-3 group">
              <div className="w-9 h-9 rounded-xl bg-[#79E6A0]/10 border border-[#79E6A0]/25 flex items-center justify-center text-[#79E6A0] shadow-[0_0_15px_rgba(121,230,160,0.15)] group-hover:scale-105 transition-transform">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#79E6A0" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M19 7V4a1 1 0 0 0-1-1H5a2 2 0 0 0 0 4h15a1 1 0 0 1 1 1v4h-3a2 2 0 0 0 0 4h3a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1" />
                  <circle cx="17" cy="14" r="1" fill="#79E6A0" />
                </svg>
              </div>
              <div>
                <div className="font-display font-bold text-base text-[#F5F7F5] group-hover:text-[#79E6A0] transition-colors flex items-center gap-1.5">
                  <span>CryptoWallet</span>
                  <span className="text-[10px] bg-[#79E6A0]/15 text-[#79E6A0] border border-[#79E6A0]/25 px-1.5 rounded font-mono font-bold">VAULT</span>
                </div>
                <span className="text-[10px] text-[#A0AAA3] tracking-wider uppercase font-mono">WEALTH & RESERVE</span>
              </div>
            </Link>
            <Link to="/" className="text-[11px] text-[#707B73] hover:text-[#79E6A0] transition-colors flex items-center gap-1.5 mt-2 pl-1">
              <i className="fas fa-arrow-left text-[9px]"></i>
              <span>Back to Landing Page</span>
            </Link>
          </div>

          {/* Vault Section Navigation */}
          <nav className="flex flex-col gap-1.5">
            <button
              type="button"
              onClick={() => setActiveTab('reserve')}
              className={`px-4 py-2.5 rounded-xl transition-all font-display text-sm font-semibold flex items-center justify-between text-left w-full ${
                activeTab === 'reserve'
                  ? 'bg-[#161B18] text-[#79E6A0] border border-[#79E6A0]/20 shadow-sm'
                  : 'text-[#A0AAA3] hover:bg-[#111513] hover:text-[#F5F7F5]'
              }`}
            >
              <span className="flex items-center gap-2.5">
                <span className="material-symbols-outlined text-[18px]">account_balance</span>
                <span>Private Reserve</span>
              </span>
              {activeTab === 'reserve' && <span className="w-1.5 h-1.5 rounded-full bg-[#79E6A0]"></span>}
            </button>

            <button
              type="button"
              onClick={() => setActiveTab('timelock')}
              className={`px-4 py-2.5 rounded-xl transition-all font-display text-sm font-semibold flex items-center justify-between text-left w-full ${
                activeTab === 'timelock'
                  ? 'bg-[#161B18] text-[#79E6A0] border border-[#79E6A0]/20 shadow-sm'
                  : 'text-[#A0AAA3] hover:bg-[#111513] hover:text-[#F5F7F5]'
              }`}
            >
              <span className="flex items-center gap-2.5">
                <span className="material-symbols-outlined text-[18px]">timelapse</span>
                <span>Time-Lock Depository</span>
              </span>
              <span className="text-xs text-[#707B73] font-mono">14D</span>
            </button>

            <button
              type="button"
              onClick={() => setActiveTab('multisig')}
              className={`px-4 py-2.5 rounded-xl transition-all font-display text-sm font-semibold flex items-center justify-between text-left w-full ${
                activeTab === 'multisig'
                  ? 'bg-[#161B18] text-[#79E6A0] border border-[#79E6A0]/20 shadow-sm'
                  : 'text-[#A0AAA3] hover:bg-[#111513] hover:text-[#F5F7F5]'
              }`}
            >
              <span className="flex items-center gap-2.5">
                <span className="material-symbols-outlined text-[18px]">group_work</span>
                <span>Multi-Sig Quorum</span>
              </span>
              <span className="text-xs text-[#14F195] font-mono">{trusteeCount}/7</span>
            </button>

            <button
              type="button"
              onClick={() => setActiveTab('audit')}
              className={`px-4 py-2.5 rounded-xl transition-all font-display text-sm font-semibold flex items-center justify-between text-left w-full ${
                activeTab === 'audit'
                  ? 'bg-[#161B18] text-[#79E6A0] border border-[#79E6A0]/20 shadow-sm'
                  : 'text-[#A0AAA3] hover:bg-[#111513] hover:text-[#F5F7F5]'
              }`}
            >
              <span className="flex items-center gap-2.5">
                <span className="material-symbols-outlined text-[18px]">verified</span>
                <span>Institutional Audit</span>
              </span>
              <span className="text-xs text-[#79E6A0] font-mono">FINMA</span>
            </button>
          </nav>

          {/* Quick Action Palette */}
          <div className="flex flex-col gap-2 pt-3 border-t border-white/5">
            <button
              type="button"
              onClick={() => handleOpenSend('BTC')}
              className="w-full bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-display font-bold py-2.5 px-4 rounded-xl flex items-center justify-center gap-2 transition-all shadow-md active:translate-y-px text-sm"
            >
              <span className="material-symbols-outlined text-[18px]">send</span>
              <span>Send / Dispatch</span>
            </button>
            <button
              type="button"
              onClick={() => handleOpenReceive('BTC')}
              className="w-full bg-[#161B18] hover:bg-[#1F2622] text-[#F5F7F5] font-display font-semibold py-2.5 px-4 rounded-xl flex items-center justify-center gap-2 transition-all border border-white/5 text-sm"
            >
              <span className="material-symbols-outlined text-[18px]">qr_code</span>
              <span>Receive / QR Code</span>
            </button>
            <button
              type="button"
              onClick={() => setIsFaucetOpen(true)}
              className="w-full bg-[#111513] hover:bg-[#161B18] text-[#14F195] font-display font-semibold py-2.5 px-4 rounded-xl flex items-center justify-center gap-2 transition-all border border-[#14F195]/20 text-sm"
            >
              <span className="material-symbols-outlined text-[18px]">water_drop</span>
              <span>Claim Testnet Faucet</span>
            </button>
          </div>

          {/* Server Links & Sidebar Logout */}
          <div className="flex flex-col gap-1 pt-3 border-t border-white/5">
            <button
              type="button"
              onClick={() => setActiveTab('reserve')}
              className="px-4 py-2 rounded-xl text-[#A0AAA3] hover:bg-[#111513] hover:text-[#F5F7F5] text-xs font-medium transition-colors flex items-center justify-between w-full text-left"
            >
              <span className="flex items-center gap-2.5">
                <i className="fas fa-wallet text-[13px]"></i>
                <span>Manage Wallets</span>
              </span>
              <span className="text-[10px] bg-[#161B18] px-1.5 py-0.5 rounded text-[#707B73] font-mono">5 Units</span>
            </button>

            <button
              type="button"
              onClick={() => setActiveTab('audit')}
              className="px-4 py-2 rounded-xl text-[#A0AAA3] hover:bg-[#111513] hover:text-[#F5F7F5] text-xs font-medium transition-colors flex items-center justify-between w-full text-left"
            >
              <span className="flex items-center gap-2.5">
                <i className="fas fa-list-check text-[13px]"></i>
                <span>Transactions Ledger</span>
              </span>
              <span className="text-[10px] bg-[#161B18] px-1.5 py-0.5 rounded text-[#707B73] font-mono">Live</span>
            </button>

            <button
              type="button"
              onClick={() => setIsAdminOpen(true)}
              className="px-4 py-2 rounded-xl text-[#79E6A0] hover:bg-[#111513] text-xs font-medium transition-colors flex items-center justify-between w-full text-left"
            >
              <span className="flex items-center gap-2.5">
                <i className="fas fa-shield-halved text-[13px]"></i>
                <span>Admin Citadel</span>
              </span>
              <span className="text-[10px] bg-[#79E6A0]/20 text-[#79E6A0] px-1.5 py-0.5 rounded font-mono font-bold">ROOT</span>
            </button>

            <button
              type="button"
              onClick={resetState}
              className="px-4 py-2 text-left rounded-xl text-[#707B73] hover:text-[#F5F7F5] hover:bg-[#111513] text-xs transition-colors flex items-center gap-2.5 w-full"
            >
              <span className="material-symbols-outlined text-[15px]">restart_alt</span>
              <span>Reset Vault State</span>
            </button>

            {/* Sidebar Logout Button */}
            <button
              type="button"
              onClick={handleLogout}
              id="sidebar-logout-btn"
              className="px-4 py-2.5 rounded-xl bg-red-500/10 hover:bg-red-500/20 text-red-400 font-display text-xs font-bold transition-all flex items-center justify-between border border-red-500/20 mt-2 shadow-sm active:translate-y-px"
              title="Secure Exit / Logout"
            >
              <span className="flex items-center gap-2.5">
                <span className="material-symbols-outlined text-[20px]">logout</span>
                <span>Sign Out / Log Out</span>
              </span>
              <span className="text-[10px] bg-red-500/20 text-red-400 px-1.5 py-0.5 rounded font-mono font-bold tracking-wider">EXIT</span>
            </button>
          </div>
        </div>

        {/* Hardware Status Strip */}
        <div className="flex flex-col gap-1.5 bg-[#111513] p-3.5 rounded-xl border border-white/5 mt-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-[#14F195] animate-pulse"></span>
              <span className="text-[11px] text-[#A0AAA3] uppercase font-mono tracking-wider">Enclave Status</span>
            </div>
            <span className="text-[10px] text-[#14F195] font-mono bg-[#161B18] px-1.5 py-0.5 rounded">LIVE</span>
          </div>
          <p className="text-xs text-[#F5F7F5]">Alpine Fort Knox Node VII Active</p>
          <div className="flex items-center justify-between text-[11px] text-[#707B73] font-mono">
            <span>Attestation: Hex-982F</span>
            <span className="text-[#14F195]">FIPS 140-3</span>
          </div>
        </div>
      </aside>

      {/* Main Content Area */}
      <div className="pl-72">
        {/* Fixed Header Bar */}
        <header className="fixed top-0 left-72 right-0 h-16 bg-[#070908]/90 backdrop-blur-xl z-40 flex items-center justify-between px-8 border-b border-white/5">
          <div className="flex items-center gap-6">
            <div className="flex items-center gap-2 bg-[#111513] px-3.5 py-1.5 rounded-xl border border-white/10">
              <span className="material-symbols-outlined text-[#14F195] text-[16px]">shield</span>
              <span className="text-xs text-[#F5F7F5] uppercase font-mono tracking-wider font-semibold">HSM Partition Verified</span>
            </div>
            <div className="hidden lg:flex items-center gap-2">
              <span className="text-xs text-[#707B73] uppercase">Tier:</span>
              <span className="text-xs font-semibold text-[#79E6A0] tracking-wide">{user?.tier || 'Private Banking Client Tier 1'}</span>
            </div>
          </div>

          <div className="flex items-center gap-4">
            <div className="hidden sm:flex items-baseline gap-2 text-right mr-2">
              <span className="text-xs text-[#707B73] uppercase tracking-wider">Aggregated Valuation:</span>
              <span className="font-display font-bold text-base text-[#F5F7F5] font-mono">
                CHF {valuation.totalChf.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
              </span>
            </div>

            {/* Profile Dropdown & Header Logout */}
            <div className="flex items-center gap-2 relative">
              <div className="relative">
                <button
                  type="button"
                  onClick={() => setIsUserMenuOpen(prev => !prev)}
                  className="flex items-center gap-2.5 bg-[#111513] hover:bg-[#161B18] py-1.5 px-3 rounded-xl border border-white/10 transition-colors"
                >
                  <div className="w-7 h-7 rounded-full bg-[#79E6A0] flex items-center justify-center text-[#0B0D0C] font-bold text-xs shadow-md">
                    <span>{user?.initials || 'AP'}</span>
                  </div>
                  <div className="flex flex-col text-left">
                    <span className="text-xs font-bold text-[#F5F7F5] leading-tight">{user?.fullName || 'Pushkaran'}</span>
                    <span className="text-[10px] text-[#707B73] font-mono">{user?.id || '24EG105R65'}</span>
                  </div>
                  <span className="material-symbols-outlined text-[16px] text-[#707B73]">expand_more</span>
                </button>

                {/* Dropdown Menu */}
                {isUserMenuOpen && (
                  <div className="absolute right-0 mt-2 w-56 bg-[#161B18] border border-white/10 rounded-2xl shadow-2xl p-2 z-50 flex flex-col gap-1 animate-fadeIn">
                    <div className="px-3 py-2 border-b border-white/10 mb-1">
                      <p className="text-xs font-bold text-[#F5F7F5]">{user?.fullName || 'Adabala Pushkaran'}</p>
                      <p className="text-[10px] text-[#707B73] font-mono">ID: {user?.id || '24EG105R65'} • Tier 1 Client</p>
                    </div>
                    <button
                      type="button"
                      onClick={() => { setActiveTab('reserve'); setIsUserMenuOpen(false); }}
                      className="w-full text-left px-3 py-2 rounded-xl hover:bg-[#1F2622] text-xs text-[#A0AAA3] hover:text-[#F5F7F5] flex items-center gap-2.5"
                    >
                      <span className="material-symbols-outlined text-[16px]">account_balance</span>
                      <span>Private Reserve</span>
                    </button>
                    <button
                      type="button"
                      onClick={() => { setActiveTab('audit'); setIsUserMenuOpen(false); }}
                      className="w-full text-left px-3 py-2 rounded-xl hover:bg-[#1F2622] text-xs text-[#A0AAA3] hover:text-[#F5F7F5] flex items-center gap-2.5"
                    >
                      <span className="material-symbols-outlined text-[16px]">verified</span>
                      <span>Audit Ledger</span>
                    </button>
                    <button
                      type="button"
                      onClick={() => { setIsAdminOpen(true); setIsUserMenuOpen(false); }}
                      className="w-full text-left px-3 py-2 rounded-xl hover:bg-[#1F2622] text-xs text-[#79E6A0] flex items-center gap-2.5 font-semibold"
                    >
                      <span className="material-symbols-outlined text-[16px]">shield</span>
                      <span>Admin Citadel</span>
                    </button>
                    <div className="border-t border-white/10 my-1"></div>
                    <button
                      type="button"
                      onClick={handleLogout}
                      className="w-full text-left px-3 py-2 rounded-xl hover:bg-red-500/15 text-xs text-red-400 flex items-center gap-2.5 font-bold"
                    >
                      <span className="material-symbols-outlined text-[20px]">logout</span>
                      <span>Sign Out / Disconnect</span>
                    </button>
                  </div>
                )}
              </div>

              {/* Direct Header Logout Button */}
              <button
                type="button"
                onClick={handleLogout}
                id="header-logout-btn"
                className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-red-500/15 hover:bg-red-500/25 text-red-400 border border-red-500/30 transition-all font-mono text-xs font-bold shadow-sm active:translate-y-px"
                title="Secure Exit / Logout"
              >
                <span className="material-symbols-outlined text-[20px]">logout</span>
                <span className="hidden sm:inline">Log Out</span>
              </button>
            </div>
          </div>
        </header>

        {/* Dynamic Workspace Container */}
        <main className="pt-20 px-8 pb-16 min-h-screen">
          {/* TAB 1: PRIVATE RESERVE */}
          {activeTab === 'reserve' && (
            <section className="flex flex-col gap-8 animate-fadeIn">
              {/* Valuation Hero Banner */}
              <div className="bg-[#161B18] border border-white/10 rounded-2xl p-8 shadow-2xl relative overflow-hidden">
                <div className="grid grid-cols-12 gap-8 items-center">
                  <div className="col-span-12 lg:col-span-8 flex flex-col gap-4">
                    <div className="flex items-center justify-between">
                      <span className="text-xs font-mono text-[#707B73] uppercase tracking-widest font-bold">PORTFOLIO VALUATION BASELINE</span>
                      <div className="flex items-center gap-2 bg-[#111513] px-3 py-1 rounded-lg border border-white/5">
                        <span className="w-1.5 h-1.5 rounded-full bg-[#14F195] animate-pulse"></span>
                        <span className="text-[10px] text-[#A0AAA3] uppercase font-mono font-semibold">ORACLE SYNCHRONIZED</span>
                      </div>
                    </div>

                    <div>
                      <div className="flex items-baseline gap-3">
                        <span className="font-display font-extrabold text-4xl sm:text-5xl text-[#79E6A0] tracking-tight font-mono">
                          ${valuation.totalUsd.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                        </span>
                        <span className="text-sm font-mono text-[#707B73]">USD</span>
                      </div>
                      <div className="flex items-center gap-4 text-xs font-mono text-[#A0AAA3] mt-2 flex-wrap">
                        <span>Eqv. <strong className="text-[#F5F7F5]">CHF {valuation.totalChf.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</strong></span>
                        <span>•</span>
                        <span className="text-[#14F195]">+3.42% 30-DAY WEIGHTED DELTA</span>
                        <span>•</span>
                        <span>Eqv. <strong className="text-[#F5F7F5]">₹{valuation.totalInr.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 })} INR</strong></span>
                      </div>
                    </div>

                    {/* Allocation Topology Bar */}
                    <div className="pt-6">
                      <div className="flex justify-between text-xs font-mono text-[#A0AAA3] mb-2">
                        <span>CUSTODIAL DISTRIBUTION TOPOLOGY</span>
                        <span className="text-[#79E6A0]">HASH: 8F2A..C39E</span>
                      </div>
                      <div className="w-full h-2.5 bg-[#111513] rounded-full overflow-hidden flex gap-0.5 p-0.5 border border-white/5">
                        <div className="h-full bg-[#79E6A0] rounded-l-full transition-all duration-700" style={{ width: `${valuation.coldPct}%` }}></div>
                        <div className="h-full bg-[#14F195] rounded-r-full transition-all duration-700" style={{ width: `${valuation.liquidPct}%` }}></div>
                      </div>
                      <div className="grid grid-cols-2 gap-4 mt-3">
                        <div className="bg-[#111513] p-3 rounded-xl border border-white/5 flex justify-between items-center text-xs font-mono">
                          <span className="text-[#A0AAA3]">Swiss Alpine Bunkers:</span>
                          <span className="text-[#79E6A0] font-bold">{valuation.coldPct.toFixed(1)}% (${valuation.coldUsd.toLocaleString()})</span>
                        </div>
                        <div className="bg-[#111513] p-3 rounded-xl border border-white/5 flex justify-between items-center text-xs font-mono">
                          <span className="text-[#A0AAA3]">Liquid Buffer Safe:</span>
                          <span className="text-[#14F195] font-bold">{valuation.liquidPct.toFixed(1)}% (${valuation.liquidUsd.toLocaleString()})</span>
                        </div>
                      </div>
                    </div>
                  </div>

                  {/* Vault Actions Card */}
                  <div className="col-span-12 lg:col-span-4 bg-[#111513] p-6 rounded-2xl border border-white/5 flex flex-col justify-between gap-6">
                    <div>
                      <span className="text-xs font-mono text-[#79E6A0] uppercase font-bold tracking-wider">FACILITY IDENTIFIER</span>
                      <h4 className="font-display font-bold text-lg text-[#F5F7F5] mt-1">Gotthard Alpine Bunker ZUR-04</h4>
                      <p className="text-xs text-[#A0AAA3] mt-2 leading-relaxed">
                        Physical Shard Layer: BIP-39 Pure Platinum Plate • Class 4 Deep-Rock HSM Tier
                      </p>
                    </div>

                    <div className="flex flex-col gap-2.5">
                      <button
                        type="button"
                        onClick={() => handleOpenSend('BTC')}
                        className="w-full bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-display font-bold py-3 rounded-xl text-sm flex items-center justify-center gap-2 transition-all shadow-md active:translate-y-px"
                      >
                        <span className="material-symbols-outlined text-[18px]">key</span>
                        <span>Initiate Capital Dispatch</span>
                      </button>
                      <button
                        type="button"
                        onClick={exportCsv}
                        className="w-full bg-[#161B18] hover:bg-[#1F2622] text-[#F5F7F5] font-display font-semibold py-2.5 rounded-xl border border-white/10 text-xs transition-colors flex items-center justify-center gap-2"
                      >
                        <span className="material-symbols-outlined text-[18px]">assignment_turned_in</span>
                        <span>Request Physical Audit Cert</span>
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              {/* Sovereign Asset Inventory Table */}
              <div className="bg-[#161B18] border border-white/10 rounded-2xl p-6 sm:p-8 shadow-xl">
                <div className="flex items-center justify-between mb-6 flex-wrap gap-4">
                  <div>
                    <span className="text-xs font-mono text-[#707B73] uppercase tracking-wider font-bold">SOVEREIGN ASSET INVENTORY</span>
                    <h3 className="font-display font-bold text-2xl text-[#F5F7F5] mt-1">Curated Reserve Ledger</h3>
                  </div>
                  <div className="flex items-center gap-2">
                    <button
                      type="button"
                      onClick={() => handleOpenReceive('BTC')}
                      className="bg-[#111513] hover:bg-[#1F2622] text-[#79E6A0] font-mono text-xs px-3.5 py-2 rounded-xl border border-[#79E6A0]/20 font-bold transition-colors"
                    >
                      + Deposit / Receive
                    </button>
                    <button
                      type="button"
                      onClick={exportCsv}
                      className="bg-[#111513] hover:bg-[#1F2622] text-[#A0AAA3] font-mono text-xs px-3.5 py-2 rounded-xl border border-white/10 transition-colors"
                    >
                      Audit Export (.CSV)
                    </button>
                  </div>
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full text-left font-body-sm">
                    <thead>
                      <tr className="border-b border-white/10 text-[#707B73] font-mono text-xs uppercase">
                        <th className="py-3 px-3">Asset & Standard</th>
                        <th className="py-3 px-3 text-right">Holdings</th>
                        <th className="py-3 px-3 text-right">Valuation (USD)</th>
                        <th className="py-3 px-3 text-right">Yield & APY</th>
                        <th className="py-3 px-3 text-right">Actions</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-white/5 font-mono text-xs">
                      {Object.values(balances).map(a => (
                        <tr key={a.code} className="hover:bg-[#111513]/60 transition-colors">
                          <td className="py-4 px-3 flex items-center gap-3">
                            <div className="w-9 h-9 rounded-xl bg-[#111513] border border-white/10 flex items-center justify-center text-lg" style={{ color: a.color }}>
                              <i className={a.icon}></i>
                            </div>
                            <div>
                              <div className="font-display font-bold text-[#F5F7F5] text-sm">{a.name}</div>
                              <span className="text-[11px] text-[#707B73]">{a.standard}</span>
                            </div>
                          </td>
                          <td className="py-4 px-3 text-right font-bold text-[#F5F7F5]">
                            {a.balance.toLocaleString(undefined, { minimumFractionDigits: 4 })} {a.code}
                          </td>
                          <td className="py-4 px-3 text-right">
                            <div className="font-bold text-[#F5F7F5]">${(a.balance * a.priceUsd).toLocaleString(undefined, { minimumFractionDigits: 2 })}</div>
                            <span className="text-[10px] text-[#707B73]">@ ${a.priceUsd.toLocaleString()} / {a.code}</span>
                          </td>
                          <td className="py-4 px-3 text-right">
                            <span className="text-[#14F195] font-bold">{a.apy ? `+${a.apy}% APY` : 'Air-Gapped'}</span>
                          </td>
                          <td className="py-4 px-3 text-right">
                            <div className="flex items-center justify-end gap-1.5">
                              <button
                                type="button"
                                onClick={() => handleOpenSend(a.code)}
                                className="p-1.5 rounded-lg bg-[#111513] hover:bg-[#79E6A0] hover:text-[#0B0D0C] text-[#F5F7F5] border border-white/10 transition-colors"
                                title={`Send ${a.code}`}
                              >
                                <span className="material-symbols-outlined text-[16px]">send</span>
                              </button>
                              <button
                                type="button"
                                onClick={() => handleOpenReceive(a.code)}
                                className="p-1.5 rounded-lg bg-[#111513] hover:bg-[#14F195] hover:text-[#0B0D0C] text-[#F5F7F5] border border-white/10 transition-colors"
                                title={`Receive ${a.code}`}
                              >
                                <span className="material-symbols-outlined text-[16px]">qr_code</span>
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </section>
          )}

          {/* TAB 2: TIME-LOCK DEPOSITORY */}
          {activeTab === 'timelock' && (
            <section className="flex flex-col gap-6 animate-fadeIn">
              <div>
                <span className="text-xs font-mono text-[#79E6A0] uppercase tracking-wider font-bold">CRYPTOGRAPHIC TIMELOCK CONTRACTS</span>
                <h2 className="font-display font-bold text-3xl text-[#F5F7F5] mt-1">Subterranean Time-Lock Depository</h2>
                <p className="text-sm text-[#A0AAA3] mt-2">
                  Air-gapped smart-contract vaults enforcing strict unbonding periods and dual-quorum withdrawal windows.
                </p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-[#161B18] p-6 rounded-2xl border border-white/10 flex flex-col justify-between">
                  <div className="flex flex-col gap-3">
                    <div className="flex justify-between items-center text-xs font-mono">
                      <span className="text-[#707B73]">VAULT-ZH-01</span>
                      <span className="bg-[#79E6A0]/15 text-[#79E6A0] px-2 py-0.5 rounded font-bold">LOCKED 30D</span>
                    </div>
                    <h3 className="font-display font-bold text-xl text-[#F5F7F5]">Gotthard Cold BTC</h3>
                    <div className="font-mono text-2xl font-bold text-[#79E6A0]">48.2500 BTC</div>
                    <p className="text-xs text-[#A0AAA3] font-mono">Eqv. $3,184,500.00 USD</p>
                    <div className="bg-[#111513] p-3 rounded-xl border border-white/5 flex justify-between text-xs font-mono">
                      <span className="text-[#707B73]">Unlocking in:</span>
                      <span className="text-[#14F195] font-bold">14d 18h 32m</span>
                    </div>
                  </div>
                  <button
                    type="button"
                    onClick={() => showToast('Time-lock armed: Mandatory consensus airgap active.', 'gold')}
                    className="w-full bg-[#111513] hover:bg-[#1F2622] text-[#A0AAA3] font-mono text-xs py-2.5 rounded-xl border border-white/10 transition-colors uppercase mt-6"
                  >
                    Time-Lock Armed (Non-Discretionary)
                  </button>
                </div>

                <div className="bg-[#161B18] p-6 rounded-2xl border border-white/10 flex flex-col justify-between">
                  <div className="flex flex-col gap-3">
                    <div className="flex justify-between items-center text-xs font-mono">
                      <span className="text-[#707B73]">VAULT-VD-03</span>
                      <span className="bg-[#14F195]/15 text-[#14F195] px-2 py-0.5 rounded font-bold">+3.85% APY</span>
                    </div>
                    <h3 className="font-display font-bold text-xl text-[#F5F7F5]">Vaduz Institutional Validator</h3>
                    <div className="font-mono text-2xl font-bold text-[#14F195]">394.1200 stETH</div>
                    <p className="text-xs text-[#A0AAA3] font-mono">Eqv. $1,379,420.00 USD</p>
                    <div className="bg-[#111513] p-3 rounded-xl border border-white/5 flex justify-between text-xs font-mono">
                      <span className="text-[#707B73]">Accrued Yield:</span>
                      <span className="text-[#79E6A0] font-bold">+15.18 stETH</span>
                    </div>
                  </div>
                  <button
                    type="button"
                    onClick={() => handleOpenSend('ETH')}
                    className="w-full bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-display font-bold text-xs py-2.5 rounded-xl transition-all uppercase mt-6"
                  >
                    Rebalance Yield Dividends
                  </button>
                </div>

                <div className="bg-[#161B18] p-6 rounded-2xl border border-white/10 flex flex-col justify-between">
                  <div className="flex flex-col gap-3">
                    <div className="flex justify-between items-center text-xs font-mono">
                      <span className="text-[#707B73]">VAULT-GN-02</span>
                      <span className="bg-orange-500/15 text-orange-400 px-2 py-0.5 rounded font-bold">+6.72% APY</span>
                    </div>
                    <h3 className="font-display font-bold text-xl text-[#F5F7F5]">Geneva Warm SOL Safe</h3>
                    <div className="font-mono text-2xl font-bold text-[#F5F7F5]">2,285.00 SOL</div>
                    <p className="text-xs text-[#A0AAA3] font-mono">Eqv. $328,200.45 USD</p>
                    <div className="bg-[#111513] p-3 rounded-xl border border-white/5 flex justify-between text-xs font-mono">
                      <span className="text-[#707B73]">Epoch Lock:</span>
                      <span className="text-[#F5F7F5] font-bold">Epoch 692 (3d left)</span>
                    </div>
                  </div>
                  <button
                    type="button"
                    onClick={() => setIsEpochOpen(true)}
                    className="w-full bg-[#111513] hover:bg-[#1F2622] text-[#F5F7F5] font-mono text-xs py-2.5 rounded-xl border border-white/10 transition-colors uppercase mt-6"
                  >
                    Inspect Epoch Manifest
                  </button>
                </div>
              </div>
            </section>
          )}

          {/* TAB 3: MULTI-SIG QUORUM */}
          {activeTab === 'multisig' && (
            <section className="flex flex-col gap-6 animate-fadeIn">
              <div>
                <span className="text-xs font-mono text-[#79E6A0] uppercase tracking-wider font-bold">CRYPTOGRAPHIC TRUSTEE COUNCIL</span>
                <h2 className="font-display font-bold text-3xl text-[#F5F7F5] mt-1">Multi-Sig Trustee Quorum ({trusteeCount} of 7 Threshold)</h2>
                <p className="text-sm text-[#A0AAA3] mt-2">
                  Dual-custody execution requires synchronous threshold signature formulation across air-gapped cryptographic signing keys.
                </p>
              </div>

              <div className="bg-[#161B18] p-8 rounded-2xl border border-white/10 shadow-xl flex flex-col gap-6">
                <div className="flex items-center justify-between border-b border-white/10 pb-6 flex-wrap gap-4">
                  <div className="flex items-center gap-4">
                    <div className="w-14 h-14 rounded-2xl bg-[#79E6A0]/10 border border-[#79E6A0]/25 flex items-center justify-center text-[#79E6A0] font-mono font-extrabold text-2xl">
                      {trusteeCount}/7
                    </div>
                    <div>
                      <h4 className="font-display font-bold text-lg text-[#F5F7F5]">Active Quorum Status: Synchronous</h4>
                      <p className="text-xs text-[#707B73] font-mono">Challenge Nonce: 0x8a0cf35422774936 • FIPS 140-3</p>
                    </div>
                  </div>
                  <button
                    type="button"
                    onClick={handleSimulateTrustee}
                    className="bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-mono text-xs px-5 py-3 rounded-xl font-bold uppercase transition-all shadow-md active:translate-y-px"
                  >
                    Simulate Trustee Sign-Off Session
                  </button>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                  {[
                    { id: '01', name: 'Zurich Private Bank AG', key: '0x98b4..c21a', hardware: 'YubiKey 5 FIPS' },
                    { id: '02', name: 'Geneva Custody Trustee', key: '0x44ae..7f90', hardware: 'HSM Partition VII' },
                    { id: '03', name: 'Vaduz Alpine Depository', key: '0x12c9..ee4b', hardware: 'NitroKey Pro Node' },
                    { id: '04', name: 'Gotthard Subterranean Safe', key: '0x33fd..8901', hardware: 'Class-4 Granite HSM' }
                  ].map((t, idx) => (
                    <div key={t.id} className="bg-[#111513] p-5 rounded-xl border border-white/5 flex flex-col gap-2">
                      <div className="flex justify-between items-center text-xs font-mono">
                        <span className="text-[#707B73]">TRUSTEE {t.id}</span>
                        <span className="bg-[#14F195]/15 text-[#14F195] px-1.5 py-0.5 rounded font-bold">READY ✓</span>
                      </div>
                      <h5 className="font-display font-bold text-sm text-[#F5F7F5] mt-1">{t.name}</h5>
                      <span className="text-[11px] font-mono text-[#707B73]">{t.key}</span>
                      <span className="text-[10px] text-[#A0AAA3] mt-2">{t.hardware}</span>
                    </div>
                  ))}
                </div>
              </div>
            </section>
          )}

          {/* TAB 4: INSTITUTIONAL AUDIT */}
          {activeTab === 'audit' && (
            <section className="flex flex-col gap-6 animate-fadeIn">
              <div>
                <span className="text-xs font-mono text-[#79E6A0] uppercase tracking-wider font-bold">REGULATORY COMPLIANCE LEDGER</span>
                <h2 className="font-display font-bold text-3xl text-[#F5F7F5] mt-1">Institutional Audit & Merkle Verification</h2>
                <p className="text-sm text-[#A0AAA3] mt-2">
                  Full cryptographic audit trail compliant with Swiss FINMA Circular 2018/3 digital custody rules.
                </p>
              </div>

              <div className="bg-[#161B18] p-8 rounded-2xl border border-white/10 shadow-xl flex flex-col gap-6">
                <div className="flex items-center justify-between flex-wrap gap-4">
                  <h3 className="font-display font-bold text-xl text-[#F5F7F5]">Confirmed Blockchain Block Ledger</h3>
                  <button
                    type="button"
                    onClick={exportCsv}
                    className="bg-[#111513] hover:bg-[#1F2622] text-[#79E6A0] font-mono text-xs px-4 py-2 rounded-xl border border-[#79E6A0]/20 font-bold transition-colors flex items-center gap-2"
                  >
                    <i className="fas fa-file-csv"></i>
                    <span>Download CSV Manifest</span>
                  </button>
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full text-left font-body-sm">
                    <thead>
                      <tr className="border-b border-white/10 text-[#707B73] font-mono text-xs uppercase">
                        <th className="py-3 px-2">Block #</th>
                        <th className="py-3 px-2">Transaction Hash</th>
                        <th className="py-3 px-2">Asset</th>
                        <th className="py-3 px-2 text-right">Amount</th>
                        <th className="py-3 px-2 text-right">Fee</th>
                        <th className="py-3 px-2 text-right">Status</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-white/5 font-mono text-xs">
                      {transactions.map(tx => (
                        <tr key={tx.id} className="hover:bg-[#111513]/60 transition-colors">
                          <td className="py-3 px-2 text-[#707B73]">#{tx.block}</td>
                          <td className="py-3 px-2 text-[#14F195] font-bold">{tx.hash}</td>
                          <td className="py-3 px-2 text-[#F5F7F5] font-bold">{tx.asset}</td>
                          <td className="py-3 px-2 text-right font-bold text-[#F5F7F5]">{tx.amount}</td>
                          <td className="py-3 px-2 text-right text-[#707B73]">{tx.fee}</td>
                          <td className="py-3 px-2 text-right">
                            <span className="bg-[#14F195]/15 text-[#14F195] text-[10px] px-2 py-0.5 rounded font-bold">{tx.status}</span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </section>
          )}
        </main>
      </div>

      {/* Global Modals */}
      <SendModal isOpen={isSendOpen} onClose={() => setIsSendOpen(false)} initialAsset={selectedAsset} />
      <ReceiveModal isOpen={isReceiveOpen} onClose={() => setIsReceiveOpen(false)} initialAsset={selectedAsset} />
      <FaucetModal isOpen={isFaucetOpen} onClose={() => setIsFaucetOpen(false)} />
      <AdminModal isOpen={isAdminOpen} onClose={() => setIsAdminOpen(false)} />
      <EpochModal isOpen={isEpochOpen} onClose={() => setIsEpochOpen(false)} />
    </div>
  );
}
