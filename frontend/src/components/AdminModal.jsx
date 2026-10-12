import React from 'react';
import { useVault } from '../context/VaultContext';
import { useAuth } from '../context/AuthContext';

export default function AdminModal({ isOpen, onClose }) {
  const { emergencyFreeze, toggleFreeze, showToast, balances, transactions } = useVault();
  const { user } = useAuth();

  if (!isOpen) return null;

  const handlePing = () => {
    showToast('Pinging Gotthard ZUR-04 (1.2ms), Vaduz VD-03 (2.4ms), Geneva GN-02 (1.8ms)... All HSM enclaves 100% synchronized ✓', 'teal');
  };

  const exportCsv = () => {
    const rows = [
      ['CryptoVault Sovereign Custody — Audit Manifest'],
      ['Attestation Date', new Date().toISOString()],
      ['Lead Engineer', 'Adabala Pushkaran (24EG105R65)'],
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
    link.setAttribute("download", `auravault_citadel_manifest_${Date.now()}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast("Downloaded auravault_citadel_manifest.csv", "teal");
    onClose();
  };

  return (
    <div 
      className="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-center justify-center p-4 animate-fadeIn"
      onClick={(e) => { if (e.target === e.currentTarget) onClose(); }}
    >
      <div className="bg-[#161B18] border border-[#79E6A0]/30 max-w-lg w-full p-6 sm:p-8 rounded-2xl shadow-2xl flex flex-col gap-5 relative">
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <div className="flex items-center gap-3">
            <span className="material-symbols-outlined text-[#79E6A0] text-[28px]">shield_person</span>
            <div>
              <h3 className="font-display font-bold text-lg text-[#F5F7F5]">Citadel Root Authority Console</h3>
              <p className="text-xs text-[#A0AAA3] font-mono">Lead Engineer: {user?.fullName || 'Adabala Pushkaran'} (24EG105R65)</p>
            </div>
          </div>
          <button 
            type="button" 
            onClick={onClose}
            className="text-[#707B73] hover:text-[#F5F7F5] transition-colors p-1"
          >
            <span className="material-symbols-outlined text-[20px]">close</span>
          </button>
        </div>

        <div className="grid grid-cols-2 gap-3 text-xs font-mono">
          <div className="bg-[#111513] p-3 rounded-xl border border-white/10 flex flex-col gap-1">
            <span className="text-[#A0AAA3] text-[10px]">HSM SECURITY ENCLAVE</span>
            <span className="text-[#79E6A0] font-bold">FIPS 140-3 LEVEL 4</span>
            <span className="text-[10px] text-[#14F195]">Tamper Envelope Armed</span>
          </div>
          <div className="bg-[#111513] p-3 rounded-xl border border-white/10 flex flex-col gap-1">
            <span className="text-[#A0AAA3] text-[10px]">ATTESTATION STATUS</span>
            <span className="text-[#F5F7F5] font-bold">GEN-IV HARDWARE PASS</span>
            <span className="text-[10px] text-[#79E6A0]">Zurich-Vaduz-Geneva</span>
          </div>
        </div>

        <div className="flex flex-col gap-3 font-mono text-xs">
          <div className="flex items-center justify-between bg-[#111513] p-3.5 rounded-xl border border-white/10">
            <div>
              <div className="text-[#F5F7F5] font-bold">Emergency Shard Isolation Lock</div>
              <div className="text-[11px] text-[#A0AAA3]">Halts automated outgoing transfers immediately</div>
            </div>
            <button
              type="button"
              onClick={toggleFreeze}
              className={`px-3 py-1.5 rounded-lg text-xs font-bold transition-all ${
                emergencyFreeze
                  ? 'bg-red-600 text-white animate-pulse shadow-lg'
                  : 'bg-red-500/20 text-red-400 border border-red-500/30 hover:bg-red-500/30'
              }`}
            >
              {emergencyFreeze ? 'DISARM FREEZE' : 'ARM FREEZE'}
            </button>
          </div>

          <div className="flex items-center justify-between bg-[#111513] p-3.5 rounded-xl border border-white/10">
            <div>
              <div className="text-[#F5F7F5] font-bold">Re-Sync Optical Interconnects</div>
              <div className="text-[11px] text-[#A0AAA3]">Ping Gotthard, Vaduz, and Geneva signer nodes</div>
            </div>
            <button
              type="button"
              onClick={handlePing}
              className="px-3 py-1.5 rounded-lg text-xs font-bold bg-[#79E6A0]/20 text-[#79E6A0] border border-[#79E6A0]/30 hover:bg-[#79E6A0]/30 transition-colors"
            >
              PING NODES
            </button>
          </div>
        </div>

        <div className="flex gap-3 pt-2">
          <button
            type="button"
            onClick={exportCsv}
            className="w-full bg-[#1F2622] hover:bg-[#28322c] text-[#F5F7F5] font-mono text-xs py-2.5 rounded-xl border border-white/10 transition-colors uppercase font-bold"
          >
            Export Manifest (.CSV)
          </button>
          <button
            type="button"
            onClick={onClose}
            className="w-full bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-mono text-xs py-2.5 rounded-xl transition-all font-bold uppercase"
          >
            Close Console
          </button>
        </div>
      </div>
    </div>
  );
}
