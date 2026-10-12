import React, { useState, useEffect, useRef } from 'react';
import { useVault } from '../context/VaultContext';

export default function ReceiveModal({ isOpen, onClose, initialAsset = 'BTC' }) {
  const { balances, showToast } = useVault();
  const [selectedAsset, setSelectedAsset] = useState(initialAsset);
  const [copied, setCopied] = useState(false);
  const canvasRef = useRef(null);

  const currentAsset = balances[selectedAsset] || balances['BTC'];

  useEffect(() => {
    if (!isOpen || !canvasRef.current || !currentAsset) return;
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const size = 160;
    const cells = 21;
    const cellSize = size / cells;
    ctx.fillStyle = "#10141a";

    let hash = 0;
    for (let i = 0; i < currentAsset.address.length; i++) {
      hash = ((hash << 5) - hash) + currentAsset.address.charCodeAt(i);
      hash |= 0;
    }

    function drawFinder(x, y) {
      ctx.fillRect(x * cellSize, y * cellSize, 7 * cellSize, 7 * cellSize);
      ctx.clearRect((x + 1) * cellSize, (y + 1) * cellSize, 5 * cellSize, 5 * cellSize);
      ctx.fillRect((x + 2) * cellSize, (y + 2) * cellSize, 3 * cellSize, 3 * cellSize);
    }
    drawFinder(0, 0);
    drawFinder(14, 0);
    drawFinder(0, 14);

    for (let r = 0; r < cells; r++) {
      for (let c = 0; c < cells; c++) {
        if ((r < 7 && c < 7) || (r < 7 && c >= 14) || (r >= 14 && c < 7)) continue;
        const bit = ((hash ^ (r * 31 + c * 17)) & 1);
        if (bit === 1) {
          ctx.fillRect(c * cellSize, r * cellSize, cellSize - 0.5, cellSize - 0.5);
        }
      }
    }
  }, [isOpen, selectedAsset, currentAsset]);

  if (!isOpen) return null;

  const handleCopy = () => {
    if (currentAsset?.address) {
      navigator.clipboard.writeText(currentAsset.address).then(() => {
        setCopied(true);
        showToast('Vault ingress address copied to clipboard!', 'teal');
        setTimeout(() => setCopied(false), 2000);
      });
    }
  };

  return (
    <div 
      className="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-center justify-center p-4 animate-fadeIn"
      onClick={(e) => { if (e.target === e.currentTarget) onClose(); }}
    >
      <div className="bg-[#161B18] border border-white/10 max-w-md w-full p-6 sm:p-8 rounded-2xl shadow-2xl flex flex-col gap-5 text-center relative">
        <div className="flex items-center justify-between border-b border-white/10 pb-4">
          <h3 className="font-display font-bold text-lg text-[#F5F7F5]">Custodial Ingress Address</h3>
          <button 
            type="button" 
            onClick={onClose}
            className="text-[#707B73] hover:text-[#F5F7F5] transition-colors p-1"
          >
            <span className="material-symbols-outlined text-[20px]">close</span>
          </button>
        </div>

        <div className="flex flex-col gap-1.5 text-left">
          <label className="text-xs text-[#A0AAA3] uppercase font-mono tracking-wider">Select Vault Depository</label>
          <select
            value={selectedAsset}
            onChange={(e) => setSelectedAsset(e.target.value)}
            className="w-full bg-[#111513] border border-white/10 text-[#F5F7F5] p-2.5 rounded-xl font-mono text-sm focus:border-[#79E6A0] focus:outline-none"
          >
            <option value="BTC">Bitcoin (BTC) — Native SegWit</option>
            <option value="ETH">Ethereum (stETH) — Validator Pool</option>
            <option value="SOL">Solana (SOL) — Warm Depository</option>
            <option value="USDT">Tether USD (USDT) — ERC-20 Safe</option>
            <option value="XRP">Ripple (XRP) — Cold Safe</option>
          </select>
        </div>

        {/* QR Code Canvas */}
        <div className="flex justify-center p-4 bg-white rounded-2xl mx-auto w-48 h-48 items-center shadow-inner">
          <canvas ref={canvasRef} width="160" height="160"></canvas>
        </div>

        <div className="flex flex-col gap-1.5 text-left">
          <span className="text-xs text-[#A0AAA3] font-mono">Vault Depository Address:</span>
          <div className="bg-[#111513] p-3 rounded-xl border border-white/10 font-mono text-xs text-[#F5F7F5] break-all flex items-center justify-between gap-2">
            <span>{currentAsset?.address}</span>
            <button
              type="button"
              onClick={handleCopy}
              className="p-1.5 text-[#79E6A0] hover:text-[#52c27b] transition-colors shrink-0"
              title="Copy Address"
            >
              <span className="material-symbols-outlined text-[18px]">
                {copied ? 'done' : 'content_copy'}
              </span>
            </button>
          </div>
        </div>

        <p className="text-[11px] text-[#A0AAA3] leading-relaxed">
          Transfers to this partition are routed through Alpine Deep-Rock HSM Node VII and reflected after 3 network confirmations.
        </p>

        <button
          type="button"
          onClick={onClose}
          className="w-full bg-[#1F2622] hover:bg-[#28322c] text-[#F5F7F5] font-display py-2.5 rounded-xl border border-white/10 transition-colors"
        >
          Done
        </button>
      </div>
    </div>
  );
}
