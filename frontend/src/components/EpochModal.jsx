import React from 'react';

export default function EpochModal({ isOpen, onClose }) {
  if (!isOpen) return null;

  return (
    <div 
      className="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-center justify-center p-4 animate-fadeIn"
      onClick={(e) => { if (e.target === e.currentTarget) onClose(); }}
    >
      <div className="bg-[#161B18] border border-white/10 max-w-md w-full p-6 sm:p-8 rounded-2xl shadow-2xl flex flex-col gap-5 relative">
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <h3 className="font-display font-bold text-lg text-[#F5F7F5]">Solana Epoch 692 Transition Manifest</h3>
          <button 
            type="button" 
            onClick={onClose}
            className="text-[#707B73] hover:text-[#F5F7F5] transition-colors p-1"
          >
            <span className="material-symbols-outlined text-[20px]">close</span>
          </button>
        </div>

        <div className="space-y-2.5 font-mono text-xs">
          <div className="bg-[#111513] p-3 rounded-xl border border-white/10 flex justify-between">
            <span className="text-[#A0AAA3]">Current Slot:</span>
            <span className="text-[#79E6A0] font-bold">298,419,012</span>
          </div>
          <div className="bg-[#111513] p-3 rounded-xl border border-white/10 flex justify-between">
            <span className="text-[#A0AAA3]">Epoch Progress:</span>
            <span className="text-[#14F195] font-bold">87.4% (3d 4h remaining)</span>
          </div>
          <div className="bg-[#111513] p-3 rounded-xl border border-white/10 flex justify-between">
            <span className="text-[#A0AAA3]">Validator Staked:</span>
            <span className="text-[#F5F7F5] font-bold">2,285.00000000 SOL</span>
          </div>
          <div className="bg-[#111513] p-3 rounded-xl border border-white/10 flex justify-between">
            <span className="text-[#A0AAA3]">Accrued Staking Reward:</span>
            <span className="text-[#79E6A0] font-bold">+153.64 SOL (Compounded)</span>
          </div>
        </div>

        <button
          type="button"
          onClick={onClose}
          className="w-full bg-[#79E6A0] hover:bg-[#52c27b] text-[#0B0D0C] font-mono text-xs py-2.5 rounded-xl transition-all font-bold uppercase mt-1"
        >
          Dismiss Manifest
        </button>
      </div>
    </div>
  );
}
