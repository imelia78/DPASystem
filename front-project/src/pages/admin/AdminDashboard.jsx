import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Check, X, Search, AlertCircle } from 'lucide-react';
import { doctorService, adminService } from '../../services/api';
import { Button } from '../../components/ui/Button';
import { Card } from '../../components/ui/Card';
import { useTranslation } from 'react-i18next';
import { extractAvatar } from '../../utils/avatarUtils';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const ControlsBar = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const SearchBox = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  padding: 0.75rem 1.25rem;
  border-radius: ${({ theme }) => theme.radii.full};
  width: 400px;

  input {
    border: none;
    outline: none;
    background: transparent;
    width: 100%;
    color: ${({ theme }) => theme.colors.text};
    font-size: 0.95rem;
  }
`;

const TableContainer = styled(Card)`
  padding: 0;
  overflow: hidden;
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
`;

const Th = styled.th`
  text-align: left;
  padding: 1.5rem;
  font-size: 0.85rem;
  font-weight: 600;
  color: ${({ theme }) => theme.colors.textMuted};
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  text-transform: uppercase;
  letter-spacing: 0.05em;
  background: ${({ theme }) => theme.colors.surfaceHover};
`;

const Td = styled.td`
  padding: 1.5rem;
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  color: ${({ theme }) => theme.colors.text};
  font-size: 0.95rem;
`;

const Tr = styled.tr`
  transition: ${({ theme }) => theme.transitions.default};
  &:hover {
    background: ${({ theme }) => theme.colors.surfaceHover};
  }
`;

const ActionButtons = styled.div`
  display: flex;
  gap: 0.5rem;
`;

const IconButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: ${({ theme }) => theme.radii.md};
  border: none;
  cursor: pointer;
  transition: ${({ theme }) => theme.transitions.default};
  color: white;

  &.approve {
    background: ${({ theme }) => theme.colors.primary};
    &:hover { background: ${({ theme }) => theme.colors.primaryHover}; }
  }

  &.reject {
    background: ${({ theme }) => theme.colors.danger};
    &:hover { background: ${({ theme }) => theme.colors.dangerHover}; }
  }
`;

const EmptyState = styled.div`
  padding: 4rem;
  text-align: center;
  color: ${({ theme }) => theme.colors.textMuted};
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
`;

// Modal Styles
const ModalOverlay = styled.div`
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContent = styled(Card)`
  width: 100%;
  max-width: 500px;
  padding: 2.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  
  h2 {
    font-size: 1.5rem;
    color: ${({ theme }) => theme.colors.text};
  }
`;

const TextArea = styled.textarea`
  width: 100%;
  height: 120px;
  padding: 1rem;
  border-radius: ${({ theme }) => theme.radii.md};
  border: 1px solid ${({ theme }) => theme.colors.border};
  resize: vertical;
  font-family: inherit;
  font-size: 1rem;
  
  &:focus {
    outline: none;
    border-color: ${({ theme }) => theme.colors.danger};
  }
`;

const ModalActions = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
`;

const AdminDashboard = () => {
  const { t } = useTranslation();
  const [doctors, setDoctors] = useState([]);
  const [search, setSearch] = useState('');
  const [rejectingDoctor, setRejectingDoctor] = useState(null);
  const [rejectComment, setRejectComment] = useState('');

  useEffect(() => {
    fetchDoctors();
  }, []);

  const fetchDoctors = async () => {
    try {
      // The backend DoctorDto omits verificationStatus. 
      // To reliably find PENDING doctors, we fetch ALL doctors and PUBLIC (Approved) doctors.
      const [allRes, approvedRes] = await Promise.all([
        adminService.getAllDoctors(100, 0),
        doctorService.getAll(100, 0)
      ]);
      
      const allDoctors = allRes.data || [];
      const approvedDoctors = approvedRes.data || [];
      const approvedIds = new Set(approvedDoctors.map(d => d.id));

      // A doctor is PENDING if:
      // 1. They are NOT in the approved list
      // 2. They do NOT have an adminComment (meaning they aren't rejected)
      const pending = allDoctors.filter(d => !approvedIds.has(d.id) && !d.adminComment);
      
      setDoctors(pending);
    } catch (err) {
      console.error("Failed to fetch doctors", err);
    }
  };

  const handleApprove = async (id) => {
    try {
      await adminService.approveDoctor(id);
      setDoctors(prev => prev.filter(d => d.id !== id));
    } catch (err) {
      console.error("Failed to approve", err);
      // If backend says already approved, remove from UI anyway
      if (err.response?.data?.includes('already approved')) {
        setDoctors(prev => prev.filter(d => d.id !== id));
      } else {
        alert("Failed to approve doctor. Please try again.");
      }
    }
  };

  const handleReject = async () => {
    if (!rejectComment.trim()) return;
    try {
      await adminService.rejectDoctor(rejectingDoctor.id, rejectComment);
      setDoctors(prev => prev.filter(d => d.id !== rejectingDoctor.id));
      setRejectingDoctor(null);
      setRejectComment('');
    } catch (err) {
      console.error("Failed to reject", err);
    }
  };

  const filteredDoctors = doctors.filter(d => 
    `${d.firstName} ${d.lastName}`.toLowerCase().includes(search.toLowerCase()) ||
    d.email.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <PageContainer>
      
      <ControlsBar>
        <SearchBox>
          <Search size={18} color="#94a3b8" />
          <input 
            placeholder={t('adminDashboard.searchPlaceholder')} 
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </SearchBox>
      </ControlsBar>

      <TableContainer>
        <Table>
          <thead>
            <tr>
              <Th>{t('adminDashboard.doctorName')}</Th>
              <Th>{t('adminDashboard.specialization')}</Th>
              <Th>{t('adminDashboard.email')}</Th>
              <Th>{t('adminDashboard.phone')}</Th>
              <Th>{t('adminDashboard.actions')}</Th>
            </tr>
          </thead>
          <tbody>
            {filteredDoctors.map(doctor => (
              <Tr key={doctor.id}>
                <Td style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  {extractAvatar(doctor.professionalDescription).photoUrl ? (
                    <img 
                      src={extractAvatar(doctor.professionalDescription).photoUrl} 
                      alt="Avatar" 
                      style={{ width: '40px', height: '40px', borderRadius: '50%', objectFit: 'cover' }} 
                    />
                  ) : (
                    <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: '#f1f5f9', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#94a3b8', fontWeight: 'bold' }}>
                      {doctor.firstName?.[0] || 'D'}
                    </div>
                  )}
                  <strong>{t('patientDashboard.dr')} {doctor.firstName} {doctor.lastName}</strong>
                </Td>
                <Td>{doctor.specialization}</Td>
                <Td>{doctor.email}</Td>
                <Td>{doctor.phoneNumber}</Td>
                <Td>
                  <ActionButtons>
                    <IconButton className="approve" onClick={() => handleApprove(doctor.id)} title={t('adminDashboard.approve')}>
                      <Check size={18} />
                    </IconButton>
                    <IconButton className="reject" onClick={() => { setRejectingDoctor(doctor); setRejectComment(''); }} title={t('adminDashboard.reject')}>
                      <X size={18} />
                    </IconButton>
                  </ActionButtons>
                </Td>
              </Tr>
            ))}
          </tbody>
        </Table>

        {filteredDoctors.length === 0 && (
          <EmptyState>
            <AlertCircle size={48} opacity={0.5} />
            <p>{t('adminDashboard.noPending')}</p>
          </EmptyState>
        )}
      </TableContainer>

      {/* Reject Modal */}
      {rejectingDoctor && (
        <ModalOverlay onClick={() => setRejectingDoctor(null)}>
          <ModalContent onClick={e => e.stopPropagation()}>
            <h2>{t('adminDashboard.rejectTitle')}</h2>
            <p style={{ color: '#64748b' }}>
              {t('adminDashboard.rejectReason1')} {rejectingDoctor.firstName} {rejectingDoctor.lastName}{t('adminDashboard.rejectReason2')}
            </p>
            <TextArea 
              placeholder={t('adminDashboard.rejectPlaceholder')} 
              value={rejectComment}
              onChange={e => setRejectComment(e.target.value)}
              autoFocus
            />
            <ModalActions>
              <Button variant="secondary" onClick={() => setRejectingDoctor(null)}>{t('adminDashboard.cancel')}</Button>
              <Button variant="danger" onClick={handleReject} disabled={!rejectComment.trim()}>{t('adminDashboard.reject')}</Button>
            </ModalActions>
          </ModalContent>
        </ModalOverlay>
      )}

    </PageContainer>
  );
};

export default AdminDashboard;
