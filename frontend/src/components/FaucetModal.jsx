import React from 'react';
import { useVault } from '../context/VaultContext';

export default function FaucetModal({ isOpen, onClose }) {
  const { claimFaucet } = useVault();

  if (!isOpen) return null;

  const handleClaim = (code, amount) => {
    claimFaucet(code, amount);
    onClose();
  };

  return (
    <div 
      className="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-center justify-center p-4 animate-fadeIn"
      onClick={(e) => { if (e.target === e.currentTarget) onClose(); }}
    >
      <div className="bg-[#161B18] border border-white/10 max-w-md w-full p-6 sm:p-8 rounded-2xl shadow-2xl flex flex-col gap-5 text-center relative">
        <div className="w-12 h-12 rounded-2xl bg-[#14F195]/15 border border-[#14F195]/30 text-[#14F195] flex items-center justify-center mx-auto text-2xl">
          <span className="material-symbols-outlined text-[24px]">water_drop</span>
        </div>

        <div>
          <h3 className="font-display font-bold text-xl text-[#F5F7F5]">Claim Testnet Dev Ingress</h3>
          <p className="text-xs text-[#A0AAA3] mt-1">Top-up your simulated Swiss vault reserve balances instantly for testing and auditing.</p>
        </div>

        <div className="grid grid-cols-2 gap-3 text-left pt-2">
          <button
            type="button"
            onClick={() => handleClaim('BTC', 0.5)}
            className="p-3.5 bg-[#111513] hover:bg-[#1F2622] rounded-xl border border-white/10 flex flex-col gap-1 transition-all group"
          >
            <span className="font-bold text-[#F7931A] font-mono text-sm group-hover:scale-105 transition-transform">+0.5 BTC</span>
            <span className="text-[10px] text-[#A0AAA3]">≈ $33,000 USD</span>
          </button>

          <button
            type="button"
            onClick={() => handleClaim('ETH', 5.0)}
            className="p-3.5 bg-[#111513] hover:bg-[#1F2622] rounded-xl border border-white/10 flex flex-col gap-1 transition-all group"
          >
            <span className="font-bold text-[#627EEA] font-mono text-sm group-hover:scale-105 transition-transform">+5.0 stETH</span>
            <span className="text-[10px] text-[#A0AAA3]">≈ $17,500 USD</span>
          </button>

          <button
            type="button"
            onClick={() => handleClaim('SOL', 25.0)}
            className="p-3.5 bg-[#111513] hover:bg-[#1F2622] rounded-xl border border-white/10 flex flex-col gap-1 transition-all group"
          >
            <span className="font-bold text-[#14F195] font-mono text-sm group-hover:scale-105 transition-transform">+25 SOL</span>
            <span className="text-[10px] text-[#A0AAA3]">≈ $3,590 USD</span>
          </button>

          <button
            type="button"
            onClick={() => handleClaim('USDT', 5000)}
            className="p-3.5 bg-[#111513] hover:bg-[#1F2622] rounded-xl border border-white/10 flex flex-col gap-1 transition-all group"
          >
            <span className="font-bold text-[#26A17B] font-mono text-sm group-hover:scale-105 transition-transform">+5,000 USDT</span>
            <span className="text-[10px] text-[#A0AAA3]">≈ $5,000 USD</span>
          </button>
        </div>

        <button
          type="button"
          onClick={onClose}
          className="w-full bg-[#1F2622] hover:bg-[#28322c] text-[#F5F7F5] font-display py-2.5 rounded-xl border border-white/10 transition-colors mt-2"
        >
          Close
        </button>
      </div>
    </div>
  );
}
